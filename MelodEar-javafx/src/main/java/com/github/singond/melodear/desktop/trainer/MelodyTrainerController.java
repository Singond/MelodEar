package com.github.singond.melodear.desktop.trainer;

import javax.inject.Inject;
import javax.inject.Provider;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.KeyedMelodyExercise;
import com.github.singond.melodear.MelodyTrainer;
import com.github.singond.melodear.desktop.audio.AudioDevice;
import com.github.singond.melodear.desktop.components.Keyboard;
import com.github.singond.melodear.desktop.keyboard.KeyboardSettings;
import com.github.singond.melodear.desktop.settings.AllSettings;

public class MelodyTrainerController {

	private static Logger logger = LogManager.getLogger(MelodyTrainerController.class);

	private MelodyTrainerModel trainerModel;

	@Inject
	AudioDevice audio;

	@Inject
	Provider<TrainerComponent.Builder> trainerProvider;

	KeyboardSettings kbdSettings;

	@FXML
	private Keyboard keyboard;

	@FXML
	private Button blah;

	@Inject
	public MelodyTrainerController(AllSettings settings) {
		kbdSettings = settings.keyboard();
	}

	public void initialize() {
		logger.debug("Setting keyboard listener");
		keyboard.setLabelFormat(kbdSettings.getKeyLabelFormat());
		kbdSettings.keyLabelFormatProperty().addListener((v, o, n) -> {
			logger.debug("Setting label format");
			keyboard.setLabelFormat(n);
		});
		initTrainer();
	}

	private void initTrainer() {
		TrainerComponent trainerComp = trainerProvider.get().build();
		trainerModel = trainerComp.getTrainerModel();
		keyboard.setListener(trainerComp.getTrainerKeyboardListener());
	}

	private MelodyTrainer<KeyedMelodyExercise> trainer() {
		final MelodyTrainerModel tm = trainerModel;
		if (tm == null) {
			throw new IllegalStateException("Trainer has not been initialized");
		}
		return tm.trainer();
	}

	public void startExercise() {
		logger.debug("'Start exercise' pressed");
	}

	public void replayKey() {
		logger.debug("'Replay key' pressed");
	}

	public void replayMelody() {
		logger.debug("Replaying melody");
		MelodyTrainer<KeyedMelodyExercise> trainer = trainer();
		if (!trainer.hasExercise()) {
			trainer.newExercise();
		}
		audio.playSequentially(trainer.getExercise().melody(), 120);
	}
}
