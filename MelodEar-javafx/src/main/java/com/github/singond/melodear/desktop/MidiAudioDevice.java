package com.github.singond.melodear.desktop;

import javax.inject.Inject;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.music.Pitch;

public class MidiAudioDevice {

	private static Logger logger = LogManager.getLogger(MidiAudioDevice.class);

	private Receiver receiver;

	@Inject
	public MidiAudioDevice() {
		try {
			receiver = MidiSystem.getReceiver();
		} catch (MidiUnavailableException e) {
			logger.error("Error obtaining MIDI receiver from system", e);
			throw new RuntimeException("Error constructing MidiAudioDevice", e);
		}
	}

	/**
	 * Plays a single indefinitely sustained note of a given pitch.
	 * <p>
	 * This sends a {@code Note On} MIDI message directly to the receiver.
	 * No {@code Note Off} event is generated.
	 *
	 * @param pitch the pitch of the note to be played
	 */
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
	public void stopNote(Pitch pitch) throws InvalidMidiDataException {
		int channel = 0;
		int velocity = 93;
		receiver.send(new ShortMessage(ShortMessage.NOTE_OFF, channel,
				pitch.midiNumber(), velocity), -1);
	}
}
