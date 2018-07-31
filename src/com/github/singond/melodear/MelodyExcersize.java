package com.github.singond.melodear;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;
import com.github.singond.music.Pitches;

/**
 * A single melodic dictation excersize to practice pitch identification.
 * This takes a simple melody (a sequence of random pitches) and expects
 * the user to identify the pitches one by one, in the order they appear
 * in the melody. Note lengths play no difference in this excersize.
 * <p>
 * The excersize starts with a sequence of notes, none of which has been
 * identified yet by the user, and evaluates notes input sequentially:
 * Once a note is received, it is compared with the note to be identified
 * (which is the first of the notes that have not been identified yet).
 * If the received note matches the pitch of the unidentified note,
 * that note is marked as identified, the next note in the melody becomes
 * the note to be identified next and the excersize waits for more input.
 * If the note received does not match the pitch of the note to be identified,
 * the excersize restarts, marking all notes as "unidentified" again.
 * Once all notes have been correctly identified, the excersize
 * is considered solved.
 *
 * @author Singon
 */
public class MelodyExcersize {

	/** The sequence of pitches comprising this melody. */
	private final List<Pitch> melody;

	/** The current note to be identified. */
	private int identify;

	private MelodyExcersize(List<Pitch> pitches) {
		this.melody = new ArrayList<>(pitches);
		identify = 0;
	}

	public static MelodyExcersize randomWithPitchClasses(
			Set<PitchClass> pitchClasses, Pitch lBound, Pitch uBound,
			int length) {
		return new MelodyExcersize(randomMelody(pitchClasses, lBound, uBound, length));
	}

	private static List<Pitch> randomMelody(Set<PitchClass> pitchClasses,
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
	 * Restarts the excersize.
	 */
	public void reset() {
		identify = 0;
	}

	public NoteEvaluationStatus evaluate(Pitch pitch) {
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
