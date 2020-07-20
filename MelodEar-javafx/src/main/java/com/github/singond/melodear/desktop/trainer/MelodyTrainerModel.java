package com.github.singond.melodear.desktop.trainer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import com.github.singond.melodear.KeyedMelodyExercise;
import com.github.singond.melodear.KeyedMelodyExerciseFactory;
import com.github.singond.melodear.MelodyTrainer;
import com.github.singond.melodear.desktop.PaneScoped;
import com.github.singond.music.Degree;
import com.github.singond.music.Keys;
import com.github.singond.music.Pitch;

@PaneScoped
public class MelodyTrainerModel {

	private static final Set<Degree> DIATONIC_DEGREES;
	private static final Set<Degree> CHROMATIC_DEGREES;
	static {
		DIATONIC_DEGREES = new HashSet<>();
		DIATONIC_DEGREES.addAll(Degree.DIATONIC_DEGREES);
		CHROMATIC_DEGREES = new HashSet<>();
		CHROMATIC_DEGREES.addAll(Degree.CHROMATIC_DEGREES_ASC);
	}
	private MelodyTrainer<KeyedMelodyExercise> trainer;

	@Inject
	public MelodyTrainerModel() {
		trainer = new MelodyTrainer<>();
		KeyedMelodyExerciseFactory factory = new KeyedMelodyExerciseFactory();
		factory.setKeysAvailable(Arrays.asList(Keys.C_MAJOR, Keys.G_MAJOR, Keys.D_MAJOR));
		factory.setDegreesAvailable(Keys.MAJOR, DIATONIC_DEGREES);
		factory.setDegreesAvailable(Keys.MINOR, DIATONIC_DEGREES);
		factory.setLowerBound(Pitch.C3);
		factory.setUpperBound(Pitch.C6);
		factory.setLength(3);
		trainer.setExerciseFactory(factory);
	}

	public MelodyTrainer<KeyedMelodyExercise> trainer() {
		return trainer;
	}
}
