package com.github.singond.melodear.desktop;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.music.Pitch;

public class MidiAudioDevice implements AudioDevice {

	private static Logger logger = LogManager.getLogger(MidiAudioDevice.class);

	private Receiver receiver;

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
		int channel = 0;
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
		int channel = 0;
		int velocity = 93;
		receiver.send(new ShortMessage(ShortMessage.NOTE_OFF, channel,
				pitch.midiNumber(), velocity), -1);
	}
}
