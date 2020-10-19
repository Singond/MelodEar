package com.github.singond.melodear.desktop.trainer;

import javafx.beans.property.IntegerProperty;

import com.github.singond.melodear.desktop.settings.PropertyAbstractSettingsTree;

public class MelodyTrainerSettings
		extends PropertyAbstractSettingsTree<MelodyTrainerSettings>{

	/** Delay before playing a new exercise in milliseconds. */
	private final IntegerProperty newExerciseDelay;

	public MelodyTrainerSettings(String name) {
		super(name);
		newExerciseDelay = newPropertyNode("newExerciseDelay", 1000);
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
}
