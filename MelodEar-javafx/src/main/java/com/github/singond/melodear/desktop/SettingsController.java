package com.github.singond.melodear.desktop;

import javax.inject.Inject;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SettingsController {

	private static Logger logger = LogManager.getLogger(SettingsController.class);

	@Inject
	KeyboardSettings kbdSettings;

	@FXML
	ChoiceBox<KeyPlayDuration> keyDuration;

	@Inject
	public SettingsController() {
		logger.debug("Creating SettingsController");
	}

	public void initialize() {
		logger.debug("Initializing key duration list");
		keyDuration.getSelectionModel().select(kbdSettings.getKeyDuration());
		kbdSettings.keyDurationProperty().bind(
				keyDuration.getSelectionModel().selectedItemProperty());
		keyDuration.setItems(FXCollections.observableArrayList(
				KeyPlayDuration.values()));
		keyDuration.setConverter(new KeyPlayDuration.Converter());
	}

}
