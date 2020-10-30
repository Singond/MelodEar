package com.github.singond.melodear.desktop.audio;

import java.util.List;

import com.github.singond.music.Pitch;
import com.github.singond.music.PitchGroup;

public interface AudioDevice {

	/**
	 * Plays a single indefinitely sustained note of a given pitch.
	 * <p>
	 * This sends a {@code Note On} MIDI message directly to the receiver.
	 * No {@code Note Off} event is generated.
	 *
	 * @param pitch the pitch of the note to be played
	 * @throws AudioException if an exception occurs with the audio device
	 */
	void playNote(Pitch pitch) throws AudioException;

	/**
	 * Mutes (stops playing) the note of a given pitch.
	 * <p>
	 * This sends a {@code Note Off} MIDI message directly to the receiver.
	 *
	 * @param pitch the pitch of the note to be stopped
	 * @throws AudioException if an exception occurs with the audio device
	 */
	void muteNote(Pitch pitch) throws AudioException;

	/**
	 * Stops playing all notes previously started by {@link #playNote}.
	 *
	 * @throws AudioException if an exception occurs with the audio device
	 */
	void muteAllNotes() throws AudioException;

	/**
	 * Plays notes of the given pitches in sequence.
	 *
	 * @param pitches the pitches to be played
	 * @param bpm the tempo to play the notes at (in notes per minute)
	 * @throws AudioException if an exception occurs with the audio device
	 */
	void playSequentially(List<? extends PitchGroup> pitches, double bpm)
			throws AudioException;

	/**
	 * Stops playback of all sequences previously started by
	 * {@link #playSequentially}.
	 *
	 * @throws AudioException if an exception occurs with the audio device
	 */
	void mutePlayback() throws AudioException;

	/**
	 * Mutes all sound.
	 *
	 * @throws AudioException if an exception occurs with the audio device
	 */
	void muteAll() throws AudioException;
}