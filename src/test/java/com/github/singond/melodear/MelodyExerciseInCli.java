package com.github.singond.melodear;

import static com.github.singond.music.PitchClass.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.github.singond.melodear.MelodyExercise.NoteEvaluationStatus;
import com.github.singond.music.Keys;
import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;

public class MelodyExerciseInCli {

	public static void main(String[] args) {
		TextMelodicTrainer trainer = new TextMelodicTrainer();
		trainer.run();
	}

	private static class TextMelodicTrainer extends InteractiveCli {

		private static final PitchParser parser = new ScientificPitchParser();

		TextMelodicTrainer() {
			exerciseFactory.setKeysAvailable(Arrays.asList(Keys.C_MAJOR,
					Keys.G_MAJOR, Keys.D_MAJOR, Keys.A_MAJOR, Keys.E_MAJOR,
					Keys.F_MAJOR, Keys.B_MAJOR, Keys.E_FLAT_MAJOR,
					Keys.A_FLAT_MAJOR));
			exerciseFactory.setLowerBound(Pitch.of(C_DBL_FLAT, 3));
			exerciseFactory.setUpperBound(Pitch.of(C_DBL_SHARP, 5));
			exerciseFactory.setLength(4);
			System.out.println("Welcome to MelodEar");
			System.out.println("This is a simple CLI imitation of real usage.");
			System.out.println("Just copy the notes you see on the screen, one by one.");
		}

		private KeyedMelodyExerciseFactory exerciseFactory = new KeyedMelodyExerciseFactory();
		private MelodyExercise exercise;
		private Set<PitchClass> pitchClasses = new HashSet<>(Arrays.asList(C, D, E, F, G, A, B));
		private Status status = Status.IDLE;

		@Override
		protected void processLine(String line) {
			if (line.equals("start")) {
				System.out.println("Starting a new melodic exercise");
				newExercise();
			} else if (line.equals("exit")) {
				System.out.println("Bye");
				terminate();
			} else if (status == Status.IDENTIFYING){
				Pitch pitch = parser.parse(line);
				NoteEvaluationStatus noteStatus = exercise.evaluate(pitch);
				switch (noteStatus) {
					case ALL_NOTES_CORRECT:
						System.out.println("Congratulations! You identified all the notes");
						newExercise();
						break;
					case NOTE_CORRECT:
						System.out.println("So far, so good");
						break;
					case NOTE_INCORRECT:
						System.out.println("Wrong");
						break;
					default:
						throw new AssertionError(noteStatus);
				}
			}
		}

		private void newExercise() {
			exercise = exerciseFactory.make();
			System.out.println("You hear: " + exercise);
			status = Status.IDENTIFYING;
		}

		private enum Status {
			IDLE,
			IDENTIFYING
		}
	}
}
