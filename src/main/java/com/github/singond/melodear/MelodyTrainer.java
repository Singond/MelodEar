package com.github.singond.melodear;

/**
 * Provider of exercises in melodic dictation.
 * Instances of this class are used to create, contain and evaluate
 * melodic dictation exercises.
 *
 * @author Singon
 * @param <T> type of exercises used by this trainer
 */
public class MelodyTrainer<T extends MelodyExercise> {

	/**
	 * Factory for creating new exercises.
	 */
	private ExerciseFactory<T> exerciseFactory;

	/**
	 * The active exercise;
	 */
	private T exercise;

	/**
	 * Returns the factory used for creating new exercises.
	 *
	 * @return the current exercise factory
	 */
	public ExerciseFactory<T> getExerciseFactory() {
		return exerciseFactory;
	}

	/**
	 * Sets the factory to be used for creating new exercises.
	 *
	 * @param factory the factory to be set
	 */
	public void setExerciseFactory(ExerciseFactory<T> factory) {
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
	public T getExercise() {
		return exercise;
	}
}
