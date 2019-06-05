package com.github.singond.melodear.desktop.piano;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.KeyboardAudio;
import com.github.singond.melodear.desktop.Main;
import com.github.singond.melodear.desktop.components.Keyboard;

import javafx.fxml.FXML;

public class PianoController {

	private static Logger logger = LogManager.getLogger(PianoController.class);

	@FXML
	private Keyboard keyboard;

	public void initialize() {
		logger.debug("Setting keyboard listener");
		keyboard.setListener(new KeyboardAudio(Main.getAudioDevice()));
	}
}
