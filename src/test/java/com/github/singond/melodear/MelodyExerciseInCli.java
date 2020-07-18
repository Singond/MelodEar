package com.github.singond.melodear;

import static com.github.singond.music.PitchClass.C_DBL_FLAT;
import static com.github.singond.music.PitchClass.C_DBL_SHARP;

import java.util.Arrays;

import com.github.singond.melodear.MelodyExercise.Status;
import com.github.singond.music.Keys;
import com.github.singond.music.Pitch;

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
			exerciseFactory.setKeyRepeat(2);
			System.out.println("Welcome to MelodEar");
			System.out.println("This is a simple CLI imitation of real usage.");
			System.out.println("Just copy the notes you see on the screen, one by one.");
		}

		private KeyedMelodyExerciseFactory exerciseFactory = new KeyedMelodyExerciseFactory();
		private MelodyExercise exercise;
		private TrainerStatus status = TrainerStatus.IDLE;

		@Override
		protected void processLine(String line) {
			if (line.equals("start")) {
				System.out.println("Starting a new melodic exercise");
				newExercise();
			} else if (line.equals("exit")) {
				System.out.println("Bye");
				terminate();
			} else if (status == TrainerStatus.IDENTIFYING){
				Pitch pitch = parser.parse(line);
				Status noteStatus = exercise.evaluate(pitch);
				switch (noteStatus) {
					case COMPLETED:
						System.out.println("Congratulations! You identified all the notes");
						newExercise();
						break;
					case NOTE_CORRECT:
						System.out.format("You have identified %d note(s) so far\n",
								exercise.notesIdentified());
						break;
					case NOTE_INCORRECT:
						System.out.println("Wrong. Start again from the beginning");
						break;
					default:
						throw new AssertionError(noteStatus);
				}
			}
		}

		private void newExercise() {
			exercise = exerciseFactory.make();
			System.out.println("You hear: " + exercise);
			status = TrainerStatus.IDENTIFYING;
		}

		private enum TrainerStatus {
			IDLE,
			IDENTIFYING
		}
	}
}
