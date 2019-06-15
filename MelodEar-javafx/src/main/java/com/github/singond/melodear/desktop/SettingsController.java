package com.github.singond.melodear.desktop;

import javax.inject.Inject;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.keyboard.KeyPlayDuration;

public class SettingsController {

	private static Logger logger = LogManager.getLogger(SettingsController.class);

	@Inject
	Settings settings;

	@FXML
	ChoiceBox<KeyPlayDuration> keyDuration;

	@Inject
	public SettingsController() {
		logger.debug("Creating SettingsController");
	}

	public void initialize() {
		logger.debug("Initializing key duration list");
		keyDuration.getSelectionModel().select(
				settings.keyboard().getKeyDuration());
		settings.keyboard().keyDurationProperty().bind(
				keyDuration.getSelectionModel().selectedItemProperty());
		keyDuration.setItems(FXCollections.observableArrayList(
				KeyPlayDuration.values()));
		keyDuration.setConverter(new KeyPlayDuration.Converter());
	}

}
