package com.github.singond.melodear;

import static com.github.singond.music.PitchClass.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.github.singond.music.Keys;
import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;

public class MelodyExcersizeCreation {

	public KeyedMelodyExcersizeFactory factory;

	@Before
	public void init() {
		factory = new KeyedMelodyExcersizeFactory();
	}

	@Test
	public void createMelody() {
		Set<PitchClass> pcs = new HashSet<>(Arrays.asList(D, E, F_SHARP, G, A, B, C_SHARP));
		Pitch lowest = Pitch.of(C, 3);
		Pitch highest = Pitch.of(C, 5);
		MelodyExcersize exc = KeyedMelodyExcersize.randomWithPitchClasses(pcs, lowest, highest, 4, null);
		System.out.format("Created random melody excersize: %s", exc);
	}

	@Test
	public void keyedExcFactory() {
		System.out.println("Setting the available keys to: C, G, c");
		factory.setKeysAvailable(Arrays.asList(Keys.C_MAJOR, Keys.G_MAJOR, Keys.C_MINOR));
		System.out.println("Setting the lower bound to C3");
		factory.setLowerBound(Pitch.C3);
		System.out.println("Setting the upper bound to C5");
		factory.setUpperBound(Pitch.C5);
		System.out.println("Setting the length to 4");
		factory.setLength(4);
		MelodyExcersize exc;

		exc = factory.allNew();
		System.out.println(exc);
		exc = factory.allNew();
		System.out.println(exc);
		exc = factory.allNew();
		System.out.println(exc);
		exc = factory.allNew();
		System.out.println(exc);

		System.out.println("Setting the length to 5");
		factory.setLength(5);

		exc = factory.allNew();
		System.out.println(exc);
		exc = factory.allNew();
		System.out.println(exc);
		exc = factory.allNew();
		System.out.println(exc);

		System.out.println("Setting the available keys to: E");
		factory.setKeysAvailable(Arrays.asList(Keys.E_MAJOR));

		exc = factory.allNew();
		System.out.println(exc);
		exc = factory.allNew();
		System.out.println(exc);

		System.out.println();
	}
}
