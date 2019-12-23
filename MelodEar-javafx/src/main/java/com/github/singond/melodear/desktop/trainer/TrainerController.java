package com.github.singond.melodear.desktop.trainer;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.audio.AudioDevice;
import com.github.singond.melodear.desktop.components.Keyboard;
import com.github.singond.melodear.desktop.keyboard.KeyboardSettings;
import com.github.singond.melodear.desktop.settings.AllSettings;
import com.github.singond.music.Pitch;

public class TrainerController {

	private static Logger logger = LogManager.getLogger(TrainerController.class);

	@Inject
	AudioDevice audio;

	@Inject
	TrainerKeyboardListener listener;

	KeyboardSettings kbdSettings;

	@FXML
	private Keyboard keyboard;

	@FXML
	private Button blah;

	@Inject
	public TrainerController(AllSettings settings) {
		kbdSettings = settings.keyboard();
	}

	public void initialize() {
		logger.debug("Setting keyboard listener");
		keyboard.setListener(listener);
		keyboard.setLabelFormat(kbdSettings.getKeyLabelFormat());
		kbdSettings.keyLabelFormatProperty().addListener((v, o, n) -> {
			logger.debug("Setting label format");
			keyboard.setLabelFormat(n);
		});
	}

	public void startExercise() {
		logger.debug("'Start exercise' pressed");
	}

	public void replayKey() {
		logger.debug("'Replay key' pressed");
	}

	public void replayMelody() {
		logger.debug("'Replay melody' pressed");
	}

	public void demo() {
		logger.debug("Playing demonstration melody");
		List<Pitch> melody = Arrays.asList(Pitch.A4, Pitch.CS5, Pitch.E5);
		audio.playSequentially(melody, 120);
	}
}
