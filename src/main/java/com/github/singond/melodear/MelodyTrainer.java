package com.github.singond.melodear;

/**
 * Provider of exercises in melodic dictation.
 * Instances of this class are used to create, contain and evaluate
 * melodic dictation exercises.
 *
 * @author Singon
 */
public class MelodyTrainer {

	/**
	 * Factory for creating new exercises.
	 */
	private MelodyExerciseFactory exerciseFactory;

	/**
	 * The active exercise;
	 */
	private MelodyExercise exercise;

	/**
	 * Returns the factory used for creating new exercises.
	 *
	 * @return the current exercise factory
	 */
	public MelodyExerciseFactory getExerciseFactory() {
		return exerciseFactory;
	}

	/**
	 * Sets the factory to be used for creating new exercises.
	 *
	 * @param factory the factory to be set
	 */
	public void setExerciseFactory(MelodyExerciseFactory factory) {
		this.exerciseFactory = factory;
	}

	/**
	 * Returns {@code true} if an exercise is ready in this trainer.
	 *
	 * @return {@code true} if {@link #getExercise} would not return {@code null}
	 */
	public boolean hasExercise() {
		return exercise != null;
	}

	public void newExercise() {
		if (exerciseFactory != null) {
			exercise = exerciseFactory.make();
		} else {
			throw new IllegalStateException("No exercise factory set");
		}
	}

	/**
	 * Returns the active exercise.
	 *
	 * @return the active exercise
	 */
	public MelodyExercise getExercise() {
		return exercise;
	}
}
