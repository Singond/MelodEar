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

class MidiAudioDevice implements AudioDevice, Closeable {

	private static Logger logger = LogManager.getLogger(MidiAudioDevice.class);

	/** Reserved controller number for "All Sound Off". */
	private static final int CONTROL_ALL_SOUND_OFF = 0x78;
	/** Meta type of the "End Of Track" MIDI message. */
	private static final int META_END_OF_TRACK = 0x2F;

	private final MidiSettings settings;
	private Synthesizer synthesizer;
	private Receiver receiver;
	private Sequencer sequencer;
	private int channel = 0;
	private int velocity = 93;

	public MidiAudioDevice() throws MidiUnavailableException {
		logger.debug("Initializing MIDI device with default settings");
		settings = null;
		receiver = MidiSystem.getReceiver();
		sequencer = MidiSystem.getSequencer();
//		sequencer.addMetaEventListener(new EndListener());
	}

	public MidiAudioDevice(MidiSettings settings) throws MidiUnavailableException {
		logger.debug("Initializing MIDI device with given settings");
		this.settings = settings;

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
		if (Files.exists(sbFile)) {
			logger.debug("Loading soundbank from {}", sbFile);
			try {
				Soundbank soundbank = MidiSystem.getSoundbank(sbFile.toFile());
				synthesizer.loadAllInstruments(soundbank);
				logger.debug("Loaded instruments from soundbank");
			} catch (InvalidMidiDataException | IOException e) {
				logger.error("Unable to load soundbank", e);
			} catch (IllegalArgumentException e) {
				logger.error("Soundbank is incompatible with current synthesizer");
			}
		}
	}

	@Override
	public void close() throws IOException {
		logger.debug("Closing synthesizer");
		synthesizer.close();
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
	public void playSequentially(List<Pitch> pitches, double bpm) {
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

	private Sequence makeSequence(List<Pitch> pitches) {
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
		for (Pitch pitch : pitches) {
			long startTicks = i;
			long endTicks = ++i;
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
}
