package com.github.singond.melodear.desktop.piano;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.AudioDevice;
import com.github.singond.melodear.desktop.KeyboardAudio;
import com.github.singond.melodear.desktop.components.Keyboard;

import javafx.fxml.FXML;

public class PianoController {

	private static Logger logger = LogManager.getLogger(PianoController.class);

	@Inject
	AudioDevice audio;

	@FXML
	private Keyboard keyboard;

	@Inject
	public PianoController() {}

	public void initialize() {
		logger.debug("Setting keyboard listener");
		keyboard.setListener(new KeyboardAudio(audio));
	}
}
