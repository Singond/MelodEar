package com.github.singond.melodear;

import com.github.singond.melodear.MelodyExercise.NoteStatus;
import com.github.singond.music.Pitch;

/**
 * Provider of exercises in melodic dictation.
 * Instances of this class are used to create, contain and evaluate
 * melodic dictation exercises.
 *
 * @param <T> type of exercises used by this trainer
 * @param <F> type of factory used to generate exercises in this trainer
 */
public interface MelodyTrainer
		<T extends MelodyExercise, F extends ExerciseFactory<T>> {

	/**
	 * Returns the factory used for creating new exercises.
	 *
	 * @return the current exercise factory
	 */
	F getExerciseFactory();

	/**
	 * Sets the factory to be used for creating new exercises.
	 *
	 * @param factory the factory to be set
	 */
	void setExerciseFactory(F factory);

	/**
	 * Returns {@code true} if an exercise is ready in this trainer.
	 *
	 * @return {@code true} if {@link #getExercise} would not throw
	 *         an {@code IllegalStateException}
	 */
	boolean hasExercise();

	/**
	 * Instantiates a new exercise in this trainer.
	 *
	 * @return the new exercise instance
	 * @throws IllegalStateException if no exercise factory has been set
	 */
	T newExercise();

	/**
	 * Returns the active exercise.
	 *
	 * @return the active exercise
	 * @throws IllegalStateException if no exercise has been initialized
	 */
	T getExercise();

	/**
	 * Evaluates the given note in the current exercise.
	 *
	 * @param pitch the note to be evaluated
	 * @return status of {@code pitch}
	 * @throws IllegalStateException if no exercise has been initialized
	 */
	NoteStatus evaluate(Pitch pitch);
}