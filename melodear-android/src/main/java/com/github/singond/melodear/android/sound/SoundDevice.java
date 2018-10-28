package com.github.singond.melodear.android.sound;

import com.github.singond.music.Pitch;

/**
 * A common interface for any sound-generating device which can be used by MelodEar.
 *
 * @author Singon
 */
public interface SoundDevice {

	void open();

	void close();

	/**
	 * Plays a single pitch with unspecified duration.
	 *
	 * @param pitch the pitch to be played
	 */
	void play(Pitch pitch);

	/**
	 * Starts playing the given pitch.
	 *
	 * @param pitch the pitch to be played
	 */
	void pitchOn(Pitch pitch);

	/**
	 * Stops playing the given pitch.
	 *
	 * @param pitch the pitch to be silenced
	 */
	void pitchOff(Pitch pitch);
}
