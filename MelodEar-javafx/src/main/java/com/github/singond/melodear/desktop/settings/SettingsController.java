package com.github.singond.melodear.desktop.settings;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.keyboard.KeyPlayDuration;

public class SettingsController {

	private static Logger logger = LogManager.getLogger(SettingsController.class);

	private Settings settings;
	private Settings settingsNew;

	@FXML
	DialogPane settingsDlg;

	@FXML
	ChoiceBox<KeyPlayDuration> keyDuration;

	public SettingsController(Settings settings) {
		logger.debug("Creating SettingsController");
		this.settings = settings;
		this.settingsNew = new Settings(settings);
	}

	public void initialize() {
		logger.debug("Initializing key duration list");
		keyDuration.getSelectionModel().select(
				settingsNew.keyboard().getKeyDuration());
		settingsNew.keyboard().keyDurationProperty().bind(
				keyDuration.getSelectionModel().selectedItemProperty());
		keyDuration.setItems(FXCollections.observableArrayList(
				KeyPlayDuration.values()));
		keyDuration.setConverter(new KeyPlayDuration.Converter());

		EventHandler<? super ActionEvent> updater
				= e -> settings.updateFrom(settingsNew);
		Button applyBtn = (Button) settingsDlg.lookupButton(ButtonType.APPLY);
		applyBtn.addEventHandler(ActionEvent.ACTION, updater);
	}

}
