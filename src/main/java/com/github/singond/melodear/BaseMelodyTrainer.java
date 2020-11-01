package com.github.singond.melodear;

import com.github.singond.melodear.MelodyExercise.NoteStatus;
import com.github.singond.music.Pitch;

/**
 * A basic implementation of {@link MelodyTrainer}.
 *
 * @param <T> type of exercises used by this trainer
 * @param <F> type of factory used to generate exercises in this trainer
 */
public class BaseMelodyTrainer
		<T extends MelodyExercise, F extends ExerciseFactory<T>>
		implements MelodyTrainer<T, F> {

	/**
	 * Factory for creating new exercises.
	 */
	private F exerciseFactory;

	/**
	 * The active exercise;
	 */
	private T exercise;

	/**
	 * Returns the factory used for creating new exercises.
	 *
	 * @return the current exercise factory
	 */
	@Override
	public F getExerciseFactory() {
		return exerciseFactory;
	}

	/**
	 * Sets the factory to be used for creating new exercises.
	 *
	 * @param factory the factory to be set
	 */
	@Override
	public void setExerciseFactory(F factory) {
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
	@Override
	public boolean hasExercise() {
		return exercise != null;
	}

	/**
	 * Instantiates a new exercise in this trainer.
	 *
	 * @return the new exercise instance
	 * @throws IllegalStateException if no exercise factory has been set
	 */
	@Override
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
	@Override
	public T getExercise() {
		if (exercise == null) {
			throw new IllegalStateException("No exercise started");
		}
		return exercise;
	}

	/**
	 * Evaluates the given note in the current exercise.
	 *
	 * @param pitch the note to be evaluated
	 * @return status of {@code pitch}
	 */
	@Override
	public NoteStatus evaluate(Pitch pitch) {
		return getExercise().evaluate(pitch);
	}
}
