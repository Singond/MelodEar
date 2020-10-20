package com.github.singond.melodear;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.github.singond.melodear.MelodyExercise.NoteStatus;
import com.github.singond.music.Pitch;

public class MelodyExerciseTest {

	private MelodyExercise exercise;

	@Before
	public void init() {
		exercise = new MelodyExercise(
				Arrays.asList(Pitch.C4, Pitch.D4, Pitch.E4)) {
			// Mockup of MelodyExercise
		};
	}

	@Test
	public void initialization() {
		assertEquals("Initial number of identified notes",
				0, exercise.notesIdentified());
	}

	@Test
	public void identifyAll() {
		NoteStatus status;
		status = exercise.evaluate(Pitch.C4);
		assertEquals("Status after identifying one note",
				NoteStatus.NOTE_CORRECT, status);
		assertEquals("Number of identified notes after identifying one",
				1, exercise.notesIdentified());

		status = exercise.evaluate(Pitch.D4);
		assertEquals("Status after identifying two notes",
				NoteStatus.NOTE_CORRECT, status);
		assertEquals("Number of identified notes after identifying two",
				2, exercise.notesIdentified());

		status = exercise.evaluate(Pitch.E4);
		assertEquals("Status after identifying all notes",
				NoteStatus.COMPLETED, status);
		assertEquals("Number of identified notes after identifying all",
				3, exercise.notesIdentified());
	}

	@Test
	public void identifyFirstWrong() {
		NoteStatus status;
		status = exercise.evaluate(Pitch.F4);
		assertEquals("Status after wrong first note",
				NoteStatus.NOTE_INCORRECT, status);
		assertEquals("Number of identified notes after wrong first note",
				0, exercise.notesIdentified());

		status = exercise.evaluate(Pitch.D4);
		assertEquals("Status after repeated wrong first note",
				NoteStatus.NOTE_INCORRECT, status);
		assertEquals("Number of identified notes after repeated wrong first note",
				0, exercise.notesIdentified());

		status = exercise.evaluate(Pitch.C4);
		assertEquals("Status after correcting first note",
				NoteStatus.NOTE_CORRECT, status);
		assertEquals("Number of identified notes after correcting first",
				1, exercise.notesIdentified());
	}

	@Test
	public void identifySecondWrong() {
		NoteStatus status;
		status = exercise.evaluate(Pitch.C4);
		status = exercise.evaluate(Pitch.A4);
		assertEquals("Status after wrong second note",
				NoteStatus.NOTE_INCORRECT, status);
		assertEquals("Number of identified notes after wrong second note",
				0, exercise.notesIdentified());

		status = exercise.evaluate(Pitch.C4);
		assertEquals("Status after reset and first note",
				NoteStatus.NOTE_CORRECT, status);
		assertEquals("Number of identified notes after reset and first note",
				1, exercise.notesIdentified());

		status = exercise.evaluate(Pitch.D4);
		assertEquals("Status after correcting second note",
				NoteStatus.NOTE_CORRECT, status);
		assertEquals("Number of identified notes after correcting second",
				2, exercise.notesIdentified());
	}

	@Test
	public void completeAfterMistakes() {
		NoteStatus status;
		status = exercise.evaluate(Pitch.F4);
		status = exercise.evaluate(Pitch.C4);
		status = exercise.evaluate(Pitch.D4);
		status = exercise.evaluate(Pitch.E4);
		assertEquals("Unable to complete after mistakes.",
				NoteStatus.COMPLETED, status);
		assertEquals("Unable to identify all notes after mistakes",
				3, exercise.notesIdentified());

	}

	@Test
	public void repeatedCompletion() {
		exercise.evaluate(Pitch.C4);
		exercise.evaluate(Pitch.D4);
		exercise.evaluate(Pitch.E4);

		// The status after calling "evaluate" on a completed exercise
		// is not specified, but the invocation should not throw an exception.
		exercise.evaluate(Pitch.E4);
	}
}
