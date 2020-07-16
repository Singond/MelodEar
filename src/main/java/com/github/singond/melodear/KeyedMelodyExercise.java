package com.github.singond.melodear;

import java.util.List;
import java.util.Set;

import com.github.singond.music.Key;
import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;

/**
 * A melody dictation exercise where the melody has an associated key.
 *
 * @author Singon
 */
public class KeyedMelodyExercise extends MelodyExercise {

	private final Key key;

	protected KeyedMelodyExercise(List<Pitch> pitches, Key key) {
		super(pitches);
		this.key = key;
	}

	public static KeyedMelodyExercise randomWithPitchClasses(
			Set<PitchClass> pitchClasses, Pitch lBound, Pitch uBound,
			int length, Key key) {
		return new KeyedMelodyExercise(randomMelody(pitchClasses, lBound, uBound, length), key);
	}

	/**
	 * Returns the reference key for this melody exercise.
	 *
	 * @return the key of this exercise
	 */
	public Key key() {
		return key;
	}

	@Override
	public String toString() {
		return String.format("%s in %s; at note %d", melody(), key, nextNoteIndex());
	}

}
