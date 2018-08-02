package com.github.singond.melodear;

import static com.github.singond.music.PitchClass.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;

public class MelodyExcersizeCreation {

	@Test
	public void createMelody() {
		Set<PitchClass> pcs = new HashSet<>(Arrays.asList(D, E, F_SHARP, G, A, B, C_SHARP));
		Pitch lowest = Pitch.of(C, 3);
		Pitch highest = Pitch.of(C, 5);
		MelodyExcersize exc = KeyedMelodyExcersize.randomWithPitchClasses(pcs, lowest, highest, 4, null);
		System.out.format("Created random melody excersize: %s", exc);
	}
}
