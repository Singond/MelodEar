package com.github.singond.melodear;

import java.util.List;
import java.util.Set;

import com.github.singond.music.Key;
import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;

/**
 * A melody dictation excersize which uses a musical key as a reference
 * for the notes to be identified.
 *
 * @author Singon
 */
public class KeyedMelodyExcersize extends MelodyExcersize {

	private final Key key;

	protected KeyedMelodyExcersize(List<Pitch> pitches, Key key) {
		super(pitches);
		this.key = key;
	}

	public static KeyedMelodyExcersize randomWithPitchClasses(
			Set<PitchClass> pitchClasses, Pitch lBound, Pitch uBound,
			int length, Key key) {
		return new KeyedMelodyExcersize(randomMelody(pitchClasses, lBound, uBound, length), key);
	}

	/**
	 * Returns the reference key for this melody excersize.
	 *
	 * @return the key of this excersize
	 */
	public Key key() {
		return key;
	}

	@Override
	public String toString() {
		return String.format("%s in %s; at note %d", melody(), key, nextNoteIndex());
	}

}
