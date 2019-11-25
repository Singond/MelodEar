package com.github.singond.melodear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;
import com.github.singond.music.Pitches;

/**
 * A single melodic dictation exercise to practice pitch identification.
 * This takes a simple melody (a sequence of random pitches) and expects
 * the user to identify the pitches one by one, in the order they appear
 * in the melody. Note lengths play no difference in this exercise.
 * <p>
 * The exercise starts with a sequence of notes, none of which has been
 * identified yet by the user, and evaluates notes input sequentially:
 * Once a note is received, it is compared with the note to be identified
 * (which is the first of the notes that have not been identified yet).
 * If the received note matches the pitch of the unidentified note,
 * that note is marked as identified, the next note in the melody becomes
 * the note to be identified next and the exercise waits for more input.
 * If the note received does not match the pitch of the note to be identified,
 * the exercise restarts, marking all notes as "unidentified" again.
 * Once all notes have been correctly identified, the exercise
 * is considered solved.
 *
 * @author Singon
 */
public abstract class MelodyExercise {

	/** The sequence of pitches comprising this melody. */
	private final List<Pitch> melody;

	/** The current note to be identified. */
	private int identify;

	protected MelodyExercise(List<Pitch> pitches) {
		this.melody = new ArrayList<>(pitches);
		identify = 0;
	}

	protected static final List<Pitch> randomMelody(Set<PitchClass> pitchClasses,
			Pitch lowerBound, Pitch upperBound, int length) {
		Random rnd = new Random();
		List<Pitch> pitches = Pitches.allBetween(lowerBound, upperBound, pitchClasses);
		List<Pitch> melody = new ArrayList<>(length);
		for (int i = 0; i < length; i++) {
			Pitch p = pitches.get(rnd.nextInt(pitches.size()));
			melody.add(p);
		}
		return melody;
	}

	/**
	 * Returns the sequence of pitches to be identified.
	 * The resulting list cannot be modified.
	 *
	 * @return an unmodifiable copy of the list of pitches to be identified
	 */
	protected final List<Pitch> melody() {
		return Collections.unmodifiableList(melody);
	}

	protected final int nextNoteIndex() {
		return identify;
	}

	/**
	 * Restarts the exercise.
	 */
	public final void reset() {
		identify = 0;
	}

	public final NoteEvaluationStatus evaluate(Pitch pitch) {
		if (melody.get(identify).equals(pitch)) {
			if (++identify < melody.size()) {
				return NoteEvaluationStatus.NOTE_CORRECT;
			} else {
				return NoteEvaluationStatus.ALL_NOTES_CORRECT;
			}
		} else {
			reset();
			return NoteEvaluationStatus.NOTE_INCORRECT;
		}
	}

	@Override
	public String toString() {
		return melody.toString() + "; at note " + identify;
	}

	public enum NoteEvaluationStatus {
		NOTE_CORRECT,
		NOTE_INCORRECT,
		ALL_NOTES_CORRECT
	}
}
