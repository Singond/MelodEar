package com.github.singond.melodear;

import static com.github.singond.music.PitchClass.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.github.singond.melodear.MelodyExcersize.NoteEvaluationStatus;
import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;

public class MelodyExcersizeInCli {

	public static void main(String[] args) {
		TextMelodicTrainer trainer = new TextMelodicTrainer();
		trainer.run();
	}

	private static class TextMelodicTrainer extends InteractiveCli {

		private static final PitchParser parser = new ScientificPitchParser();

		TextMelodicTrainer() {
			System.out.println("Welcome to MelodEar");
			System.out.println("This is a simple CLI imitation of real usage.");
			System.out.println("Just copy the notes you see on the screen, one by one.");
		}

		private MelodyExcersize excersize;
		private Set<PitchClass> pitchClasses = new HashSet<>(Arrays.asList(C, D, E, F, G, A, B));
		private Pitch lowest = Pitch.of(C_DBL_FLAT, 3);
		private Pitch highest = Pitch.of(C_DBL_SHARP, 5);
		private int length = 4;
		private Status status = Status.IDLE;

		@Override
		protected void processLine(String line) {
			if (line.equals("start")) {
				System.out.println("Starting a new melodic excersize");
				newExcersize();
			} else if (line.equals("exit")) {
				System.out.println("Bye");
				terminate();
			} else if (status == Status.IDENTIFYING){
				Pitch pitch = parser.parse(line);
				NoteEvaluationStatus noteStatus = excersize.evaluate(pitch);
				switch (noteStatus) {
					case ALL_NOTES_CORRECT:
						System.out.println("Congratulations! You identified all the notes");
						newExcersize();
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

		private void newExcersize() {
			excersize = KeyedMelodyExcersize.randomWithPitchClasses(pitchClasses, lowest, highest, length, null);
			System.out.println("You hear: " + excersize);
			status = Status.IDENTIFYING;
		}

		private enum Status {
			IDLE,
			IDENTIFYING
		}
	}
}
