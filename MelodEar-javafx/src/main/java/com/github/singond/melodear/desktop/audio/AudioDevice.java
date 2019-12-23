package com.github.singond.melodear.desktop.audio;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;

import com.github.singond.music.Pitch;

public interface AudioDevice {

	/**
	 * Plays a single indefinitely sustained note of a given pitch.
	 * <p>
	 * This sends a {@code Note On} MIDI message directly to the receiver.
	 * No {@code Note Off} event is generated.
	 *
	 * @param pitch the pitch of the note to be played
	 */
	void playNote(Pitch pitch) throws InvalidMidiDataException;

	/**
	 * Mutes (stops playing) the note of a given pitch.
	 * <p>
	 * This sends a {@code Note Off} MIDI message directly to the receiver.
	 *
	 * @param pitch the pitch of the note to be stopped
	 */
	void stopNote(Pitch pitch) throws InvalidMidiDataException;

	/**
	 * Stops playing all notes.
	 */
	public void stopAllNotes() throws InvalidMidiDataException;

	/**
	 * Plays notes of the given pitches in sequence.
	 *
	 * @param pitches the pitches to be played
	 * @param bpm the tempo to play the notes at (in notes per minute)
	 */
	void playSequentially(List<Pitch> pitches, double bpm);
}