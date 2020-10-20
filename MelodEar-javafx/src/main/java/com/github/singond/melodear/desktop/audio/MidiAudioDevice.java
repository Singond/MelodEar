package com.github.singond.melodear.desktop.audio;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.music.Pitch;
import com.github.singond.music.PitchGroup;

class MidiAudioDevice implements AudioDevice, Closeable {

	private static Logger logger = LogManager.getLogger(MidiAudioDevice.class);

	/** Reserved controller number for "All Sound Off". */
	private static final int CONTROL_ALL_SOUND_OFF = 0x78;
	/** Meta type of the "End Of Track" MIDI message. */
	private static final int META_END_OF_TRACK = 0x2F;

	private Synthesizer synthesizer;
	private Receiver receiver;
	private Sequencer sequencer;
	private int channel = 0;
	private int velocity = 93;
	private SoundbankStatus soundbankStatus;

	public MidiAudioDevice() throws MidiUnavailableException {
		logger.debug("Initializing MIDI device with default settings");
		receiver = MidiSystem.getReceiver();
		sequencer = MidiSystem.getSequencer();
//		sequencer.addMetaEventListener(new EndListener());
	}

	public MidiAudioDevice(MidiSettings settings)
			throws MidiUnavailableException {
		configure(settings);
	}

	public final void configure(MidiSettings settings)
			throws MidiUnavailableException {
		logger.debug("Initializing MIDI device with given settings");
		// Initialize synthesizer
		MidiDevice.Info synthInfo = settings.getSynth();
		if (synthInfo != null) {
			try (MidiDevice device = MidiSystem.getMidiDevice(synthInfo)) {
				if (device != null && device instanceof Synthesizer) {
					logger.debug("Obtaining selected synthesizer: {}",
							synthInfo.getName());
					synthesizer = (Synthesizer) device;
				}
			}
		}
		if (synthesizer == null) {      // Fallback if the above fails
			logger.debug("Obtaining default synthesizer");
			synthesizer = MidiSystem.getSynthesizer();
		}
		if (!synthesizer.isOpen()) {
			logger.debug("Opening synthesizer");
			synthesizer.open();
		}
		receiver = synthesizer.getReceiver();
		sequencer = MidiSystem.getSequencer();
		sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());

		// Load soundbank
		Path sbFile = settings.getSoundbank();
		if (sbFile != null && Files.exists(sbFile)) {
			logger.debug("Loading soundbank from {}", sbFile);
			try {
				Soundbank soundbank = MidiSystem.getSoundbank(sbFile.toFile());
				if (synthesizer.loadAllInstruments(soundbank)) {
					soundbankStatus = SoundbankStatus.LOADED;
				} else {
					soundbankStatus = SoundbankStatus.ERROR_LOADING;
				}
				logger.debug("Loaded instruments from soundbank");
			} catch (InvalidMidiDataException e) {
				soundbankStatus = SoundbankStatus.DATA_INVALID;
				logger.error("Soundbank is incompatible with current MIDI system");
			} catch (IllegalArgumentException | IOException e) {
				soundbankStatus = SoundbankStatus.DATA_INVALID;
				logger.error("Unable to load soundbank", e);
			}
		} else {
			soundbankStatus = SoundbankStatus.FILE_NOT_FOUND;
		}
	}

	@Override
	public void close() throws IOException {
		logger.debug("Closing synthesizer");
		synthesizer.close();
	}

	/**
	 * Returns the status of the soundbank which was present in the most
	 * recent settings object used to call {@link #configure}.
	 *
	 * @return the status of the last soundbank
	 */
	public SoundbankStatus getSoundbankStatus() {
		return soundbankStatus;
	}

	/**
	 * Plays a single indefinitely sustained note of a given pitch.
	 * <p>
	 * This sends a {@code Note On} MIDI message directly to the receiver.
	 * No {@code Note Off} event is generated.
	 *
	 * @param pitch the pitch of the note to be played
	 */
	@Override
	public void playNote(Pitch pitch) throws InvalidMidiDataException {
		receiver.send(new ShortMessage(ShortMessage.NOTE_ON, channel,
				pitch.midiNumber(), velocity), -1);
	}

	/**
	 * Mutes (stops playing) the note of a given pitch.
	 * <p>
	 * This sends a {@code Note Off} MIDI message directly to the receiver.
	 *
	 * @param pitch the pitch of the note to be stopped
	 */
	@Override
	public void stopNote(Pitch pitch) throws InvalidMidiDataException {
		receiver.send(new ShortMessage(ShortMessage.NOTE_OFF, channel,
				pitch.midiNumber(), velocity), -1);
	}

	@Override
	public void stopAllNotes() throws InvalidMidiDataException {
		receiver.send(new ShortMessage(ShortMessage.CONTROL_CHANGE,
				channel, CONTROL_ALL_SOUND_OFF, 0), -1);
	}

	@Override
	public void playSequentially(List<? extends PitchGroup> pitches, double bpm) {
		try {
			if (!sequencer.isOpen()) {
				sequencer.open();
			}
			sequencer.setTempoInBPM((float)bpm);
			sequencer.setSequence(makeSequence(pitches));
			// Play the sequence
			sequencer.start();
		} catch (InvalidMidiDataException e) {
			logger.error("Cannot load sequence into sequencer", e);
		} catch (MidiUnavailableException e) {
			logger.error("Cannot open sequencer", e);
		}
	}

	private Sequence makeSequence(List<? extends PitchGroup> pitches) {
		Sequence sequence;
		try {
			// One tick per quarter note; one track
			sequence = new Sequence(Sequence.PPQ, 1, 1);
		} catch (InvalidMidiDataException e) {
			logger.error("Error when constructing MIDI sequence", e);
			return null;
		}
		// The constructor ensures there is one track in the sequence
		Track track = sequence.getTracks()[0];

		// Put all pitches into the track, one after anonether
		int i = 0;
		for (PitchGroup pitchGrp : pitches) {
			long startTicks = i;
			long endTicks = ++i;
			for (Pitch pitch : pitchGrp) {
				ShortMessage startMsg, endMsg;
				try {
					startMsg = new ShortMessage(ShortMessage.NOTE_ON, channel,
							pitch.midiNumber(), velocity);
					endMsg = new ShortMessage(ShortMessage.NOTE_OFF, channel,
							pitch.midiNumber(), velocity);
				} catch (InvalidMidiDataException e) {
					logger.error("Error when constructing MIDI message", e);
					e.printStackTrace();
					return null;
				}
				track.add(new MidiEvent(startMsg, startTicks));
				track.add(new MidiEvent(endMsg, endTicks));
			}
		}
		return sequence;
	}

	private class EndListener implements MetaEventListener {
		@Override
		public void meta(MetaMessage meta) {
			if (meta.getType() == META_END_OF_TRACK) {
				logger.debug("End of track");
			}
		}
	}

	/**
	 * Status of loading a soundbank file into a synthesizer.
	 */
	public enum SoundbankStatus {
		/** The soundbank has been loaded correctly. */
		LOADED(true),
		/** The data in the file were not recognized as soundbank. */
		DATA_INVALID(false),
		/** The soundbank file does not exist. */
		FILE_NOT_FOUND(false),
		/** An error occured when loading the soundbank. */
		ERROR_LOADING(false),
		/** Other error. */
		ERROR(false);

		private final boolean valid;
		private final String key;

		private SoundbankStatus(boolean valid, String key) {
			this.valid = valid;
			this.key = key;
		}

		private SoundbankStatus(boolean valid) {
			this.valid = valid;
			this.key = name().toLowerCase();
		}

		/**
		 * Returns {@code true} if this status represents a soundbank
		 * successfully loaded into the synthesizer.
		 *
		 * @return {@code true} if this status represents a valid soundbank
		 */
		public boolean valid() {
			return valid;
		}

		public String key() {
			return key;
		}
	}
}
