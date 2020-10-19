package com.github.singond.melodear.desktop.trainer;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MelodyTrainerSettingsController {

	private static Logger logger = LogManager.getLogger(
			MelodyTrainerSettingsController.class);

	private final MelodyTrainerSettings settings;

	@FXML Spinner<Integer> newExerciseDelay;

	public MelodyTrainerSettingsController(MelodyTrainerSettings settings) {
		logger.debug("Creating MelodyTrainerSettingsController");
		this.settings = settings;
	}

	public void initialize() {
		// Delay before new exercise
		settings.newExerciseDelayProperty().bind(
				newExerciseDelay.valueProperty());
	}
}
