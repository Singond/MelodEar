package com.github.singond.melodear;

import static com.github.singond.music.PitchClass.*;
import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.github.singond.music.Degree;
import com.github.singond.music.Keys;
import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;
import com.github.singond.music.Pitches;

public class MelodyExerciseCreation {

	private static Logger logger = LogManager.getLogger(MelodyExerciseCreation.class);

	public KeyedMelodyExerciseFactory factory;

	@Before
	public void init() {
		factory = new KeyedMelodyExerciseFactory();
	}

	@Test
	public void createMelody() {
		logger.info("Creating dummy melody exercise without any key:");
		Set<PitchClass> pcs = new HashSet<>(Arrays.asList(D, E, F_SHARP, G, A, B, C_SHARP));
		Pitch lowest = Pitch.of(C, 3);
		Pitch highest = Pitch.of(C, 5);
		MelodyExercise exc = KeyedMelodyExercise.randomWithPitchClasses(pcs, lowest, highest, 4, null);
		logger.info("Created random melody exercise: {}", exc);
		logger.info("");
	}

	@Test
	public void keyedExcFactory() {
		logger.info("Using factory to generate melody exercises in key:");
		logger.info("Setting the available keys to: C, G, c");
		factory.setKeysAvailable(Arrays.asList(Keys.C_MAJOR, Keys.G_MAJOR, Keys.C_MINOR));
		logger.info("Setting the lower bound to C3");
		factory.setLowerBound(Pitch.C3);
		logger.info("Setting the upper bound to C5");
		factory.setUpperBound(Pitch.C5);
		logger.info("Setting the length to 4");
		factory.setLength(4);
		logger.info("Generating key");
		factory.newKey();
		MelodyExercise exc;

		exc = factory.make();
		logger.info(exc.toString());
		exc = factory.make();
		logger.info(exc.toString());
		exc = factory.make();
		logger.info(exc.toString());
		exc = factory.make();
		logger.info(exc.toString());

		logger.info("Setting the length to 5");
		factory.setLength(5);

		exc = factory.make();
		logger.info(exc.toString());
		exc = factory.make();
		logger.info(exc.toString());
		exc = factory.make();
		logger.info(exc.toString());

		logger.info("Setting lower bound to C4");
		factory.setLowerBound(Pitch.C4);

		exc = factory.make();
		logger.info(exc.toString());
		exc = factory.make();
		logger.info(exc.toString());
		exc = factory.make();
		logger.info(exc.toString());
		logger.info(exc.toString());

		logger.info("Setting the available keys to: E");
		factory.setKeysAvailable(Arrays.asList(Keys.E_MAJOR));
		factory.newKey();

		exc = factory.make();
		logger.info(exc.toString());
		exc = factory.make();
		logger.info(exc.toString());

		logger.info("Setting available degrees to I, III and V for major keys");
		factory.setDegreesAvailable(Keys.MAJOR, new HashSet<>(
				Arrays.asList(Degree.I, Degree.III, Degree.V)));

		exc = factory.make();
		logger.info(exc.toString());
		exc = factory.make();
		logger.info(exc.toString());
		exc = factory.make();
		logger.info(exc.toString());
		exc = factory.make();
		logger.info(exc.toString());
		exc = factory.make();

		logger.info("");
	}

	@Test
	public void autoRepeatedKey() {
		factory.setKeysAvailable(Arrays.asList(Keys.C_MAJOR,
				Keys.G_MAJOR, Keys.D_MAJOR, Keys.A_MAJOR, Keys.E_MAJOR));
		factory.setKeyRepeat(2);
		for (int i = 1; i < 10; i++) {
			KeyedMelodyExercise exc1 = factory.make();
			assertTrue(factory.isKeyNew());
			KeyedMelodyExercise exc2 = factory.make();
			assertFalse(factory.isKeyNew());
			assertEquals(exc1.key(), exc2.key());
		}
	}

	@Test
	public void manualRepeatedKey() {
		factory.setKeysAvailable(Arrays.asList(Keys.C_MAJOR));
		factory.setKeyRepeat(2);

		factory.setKey(Keys.F_MAJOR);
		KeyedMelodyExercise exc1 = factory.make();
		assertEquals(Keys.F_MAJOR, exc1.key());
		assertTrue(factory.isKeyNew());
		KeyedMelodyExercise exc2 = factory.make();
		assertFalse(factory.isKeyNew());
		assertEquals(Keys.F_MAJOR, exc2.key());

		factory.setKey(Keys.B_MAJOR);
		exc1 = factory.make();
		assertEquals(Keys.B_MAJOR, exc1.key());
		assertTrue(factory.isKeyNew());
		exc2 = factory.make();
		assertFalse(factory.isKeyNew());
		assertEquals(Keys.B_MAJOR, exc2.key());
	}

	@Test
	public void maxIntervalSelection() {
		factory.setKeysAvailable(Arrays.asList(Keys.G_MAJOR));
		List<Pitch> pitchesAvailable
			= Pitches.allBetween(Pitch.C3, Pitch.C6, Keys.G_MAJOR.pitchClasses());
		Object selected;

		selected = invokeSelectPitches(factory, pitchesAvailable,
				Pitch.G3, Pitch.B3);
		assertEquals("Wrong result of selectPitches",
				Arrays.asList(Pitch.G3, Pitch.A3, Pitch.B3), selected);

		selected = invokeSelectPitches(factory, pitchesAvailable,
				Pitch.C3, Pitch.E3);
		assertEquals("Wrong result of selectPitches",
				Arrays.asList(Pitch.C3, Pitch.D3, Pitch.E3), selected);

		selected = invokeSelectPitches(factory, pitchesAvailable,
				Pitch.C3, Pitch.C3);
		assertEquals("Wrong result of selectPitches",
				Arrays.asList(Pitch.C3), selected);

		selected = invokeSelectPitches(factory, pitchesAvailable,
				Pitch.C6, Pitch.C6);
		assertEquals("Wrong result of selectPitches",
				Arrays.asList(Pitch.C6), selected);
	}

	private Object invokeSelectPitches(KeyedMelodyExerciseFactory factory,
		List<Pitch> pitches, Pitch from, Pitch to) {
		Method method;
		try {
			method = factory.getClass().getDeclaredMethod("selectPitches",
					List.class, Pitch.class, Pitch.class);
			method.setAccessible(true);
			return method.invoke(factory, pitches, from, to);
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			logger.error("Error invoking selectPitches", e);
			fail("Error in test: Cannot invoke selectPitches");
		}
		return null;
	}
}
