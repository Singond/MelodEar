package com.github.singond.melodear;

/**
 * A generic factory for musical exercises.
 * This factory keeps track of constraints for creating musical trainer
 * exercises and creates new exercises based on these constraints.
 *
 * @author Singon
 * @param <T> the type of exercises provided by this factory
 */
// TODO: Use an upper bounded wildcard for T
public interface ExerciseFactory<T> {

	/**
	 * Creates a new musical exercise based on the current constraints.
	 *
	 * @return a new musical exercise
	 */
	T make();

}
