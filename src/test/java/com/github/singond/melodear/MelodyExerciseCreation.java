package com.github.singond.melodear;

import static com.github.singond.music.PitchClass.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.singond.music.Degree;
import com.github.singond.music.Keys;
import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;

public class MelodyExerciseCreation {

	private static Logger logger = LoggerFactory.getLogger(MelodyExerciseCreation.class);

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
}
