package com.github.singond.melodear;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;

/**
 * A single melodic dictation excersize to practice relative pitch.
 * This creates a random melody based on some constraints, makes it
 * available for playback and waits for user input in the form of notes.
 * The notes received are compared with the melody for correctness.
 * The answer is considered correct if the pitches of the sequence of notes
 * match the pitches of the melody exactly, in the same order.
 *
 * @author Singon
 */
public class MelodyExcersize {

	/** The sequence of pitches comprising this melody. */
	private final List<Pitch> melody;

	private MelodyExcersize(List<Pitch> pitches) {
		this.melody = pitches;
	}

	public static MelodyExcersize randomWithPitchClasses(
			Set<PitchClass> pitchClasses, Pitch lBound, Pitch uBound) {
		return new MelodyExcersize(randomMelody(pitchClasses, lBound, uBound, 0));
	}

	private static List<Pitch> randomMelody(Set<PitchClass> pitchClasses,
			Pitch lowerBound, Pitch upperBound, int length) {
		Random rnd = new Random();
		List<PitchClass> pcs = new ArrayList<>(pitchClasses);
		List<Pitch> melody = new ArrayList<>(length);
		for (int i = 0; i < length; i++) {
//			PitchClass
		}
		throw new UnsupportedOperationException("This method has not been implemented yet");

	}
}
