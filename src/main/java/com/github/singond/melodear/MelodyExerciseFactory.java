package com.github.singond.melodear;

/**
 * A factory for melody exercises.
 * This object keeps track of constraints for creating melodic exercises
 * and creates new instances of melody exercise based on these constraints.
 *
 * @author Singon
 */
public interface MelodyExerciseFactory {

	/**
	 * Creates a new melody exercise based on the current constraints.
	 *
	 * @return a new melody exercise
	 */
	MelodyExercise make();

}
