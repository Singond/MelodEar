package com.github.singond.melodear.desktop.keyboard;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KeyboardSettingsController {

	private static Logger logger = LogManager.getLogger(KeyboardSettingsController.class);

	private final KeyboardSettings settings;

	@FXML
	ChoiceBox<KeyPlayDuration> keyDuration;

	public KeyboardSettingsController(KeyboardSettings settings) {
		logger.debug("Creating KeyboardSettingsController");
		this.settings = settings;
	}

	public void initialize() {
		logger.debug("Initializing key duration list");
		keyDuration.getSelectionModel().select(settings.getKeyDuration());
		settings.keyDurationProperty().bind(
				keyDuration.getSelectionModel().selectedItemProperty());
		keyDuration.setItems(FXCollections.observableArrayList(
				KeyPlayDuration.values()));
		keyDuration.setConverter(new KeyPlayDuration.Converter());
	}
}
