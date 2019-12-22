package com.github.singond.melodear.desktop.audio;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.music.Pitch;

class MidiAudioDevice implements AudioDevice {

	private static Logger logger = LogManager.getLogger(MidiAudioDevice.class);

	/** Reserved controller number for "all sound off". */
	private static final int CONTROL_ALL_SOUND_OFF = 0x78;

	private Receiver receiver;
	private int channel;

	public MidiAudioDevice() throws MidiUnavailableException {
		logger.debug("Obtaining default MIDI receiver from system");
		receiver = MidiSystem.getReceiver();
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
		int velocity = 93;
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
		int velocity = 93;
		receiver.send(new ShortMessage(ShortMessage.NOTE_OFF, channel,
				pitch.midiNumber(), velocity), -1);
	}

	@Override
	public void stopAllNotes() throws InvalidMidiDataException {
		receiver.send(new ShortMessage(ShortMessage.CONTROL_CHANGE,
				channel, CONTROL_ALL_SOUND_OFF, 0), -1);
	}
}
