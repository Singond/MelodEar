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
		if (factory == null) {
			throw new NullPointerException("Factory must not be null");
		}
		this.exerciseFactory = factory;
	}

	/**
	 * Returns {@code true} if an exercise is ready in this trainer.
	 *
	 * @return {@code true} if {@link #getExercise} would not throw
	 *         an {@code IllegalStateException}
	 */
	public boolean hasExercise() {
		return exercise != null;
	}

	/**
	 * Instantiates a new exercise in this trainer.
	 *
	 * @return the new exercise instance
	 * @throws IllegalStateException if no exercise factory has been set
	 */
	public T newExercise() {
		if (exerciseFactory != null) {
			return exercise = exerciseFactory.make();
		} else {
			throw new IllegalStateException("No exercise factory set");
		}
	}

	/**
	 * Returns the active exercise.
	 *
	 * @return the active exercise
	 * @throws IllegalStateException if no exercise has been initialized
	 */
	public T getExercise() {
		if (exercise == null) {
			throw new IllegalStateException("No exercise started");
		}
		return exercise;
	}
}
