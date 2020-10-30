package com.github.singond.melodear.desktop.trainer;

import javafx.beans.property.IntegerProperty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.settings.PropertyAbstractSettingsTree;

public class MelodyTrainerSettings
		extends PropertyAbstractSettingsTree<MelodyTrainerSettings>{

	private static Logger logger = LogManager.getLogger(MelodyTrainerSettings.class);

	/** Delay before playing a new exercise in milliseconds. */
	private final IntegerProperty newExerciseDelay;

	/** Number of notes in a melody. */
	private final IntegerProperty melodyLength;

	public MelodyTrainerSettings(String name) {
		super(name);
		logger.debug("Initializing MelodyTrainerSettings");
		newExerciseDelay = newPropertyNode("newExerciseDelay", 1000);
		melodyLength = newPropertyNode("melodyLength", 3);
	}

	public MelodyTrainerSettings() {
		this(MelodyTrainerSettings.class.getName());
	}

	@Override
	protected MelodyTrainerSettings newInstance(String key) {
		return new MelodyTrainerSettings(key);
	}

	public int getNewExerciseDelay() {
		return newExerciseDelay.get();
	}

	public void setNewExerciseDelay(int millis) {
		newExerciseDelay.set(millis);
	}

	public IntegerProperty newExerciseDelayProperty() {
		return newExerciseDelay;
	}

	public int getMelodyLength() {
		return melodyLength.get();
	}

	public void setMelodyLength(int millis) {
		melodyLength.set(millis);
	}

	public IntegerProperty melodyLengthProperty() {
		return melodyLength;
	}
}
