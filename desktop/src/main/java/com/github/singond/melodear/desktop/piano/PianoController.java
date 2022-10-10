package com.github.singond.melodear.desktop.piano;

import javax.inject.Inject;

import javafx.fxml.FXML;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.components.Keyboard;
import com.github.singond.melodear.desktop.keyboard.KeyboardSettings;
import com.github.singond.melodear.desktop.settings.AllSettings;

public class PianoController {

	private static Logger logger = LogManager.getLogger(PianoController.class);

	@Inject
	PianoKeyboardListener listener;

	KeyboardSettings kbdSettings;

	@FXML
	private Keyboard keyboard;

	@Inject
	public PianoController(AllSettings settings) {
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
}
