package com.github.singond.melodear.desktop.settings;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.keyboard.KeyPlayDuration;

public class SettingsController {

	private static Logger logger = LogManager.getLogger(SettingsController.class);

	private AllSettings settings;
	private AllSettings settingsNew;

	@FXML
	DialogPane settingsDlg;

	@FXML
	BorderPane settingsPane;

	@FXML
	ListView<SettingsView> sectionSelect;

	@FXML
	ChoiceBox<KeyPlayDuration> keyDuration;

	public SettingsController(AllSettings settings) {
		logger.debug("Creating SettingsController");
		this.settings = settings;
		this.settingsNew = new AllSettings(settings);
	}

	public void initialize() {
		ObservableList<SettingsView> sectionsObs
				= FXCollections.observableArrayList(buildSections());
		sectionSelect.setItems(sectionsObs);
		sectionSelect.getSelectionModel().selectedItemProperty()
				.addListener((v, o, n) -> {
					logger.debug("Selected {}", n);
					settingsPane.setCenter(n.getNode());
				});

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

	private List<SettingsView> buildSections() {
		List<SettingsView> sections = new ArrayList<>();
//		sections.add(new SettingsSection());
//		sections.add(new SettingsSection());
		return sections;
	}
}
