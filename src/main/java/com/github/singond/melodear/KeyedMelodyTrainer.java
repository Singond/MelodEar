package com.github.singond.melodear;

public class KeyedMelodyTrainer
		extends BaseMelodyTrainer
				<KeyedMelodyExercise, KeyedMelodyExerciseFactory> {

	/**
	 * Returns {@code true} if the current exercise is the first since
	 * a new key has been generated.
	 *
	 * @return {@code true} if the current exercise is in a new key
	 */
	public boolean isKeyNew() {
		return getExerciseFactory().isKeyNew();
	}
}
