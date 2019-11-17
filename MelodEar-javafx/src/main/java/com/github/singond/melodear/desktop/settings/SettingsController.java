package com.github.singond.melodear.desktop.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.Main;
import com.github.singond.melodear.desktop.audio.MidiSettingsView;
import com.github.singond.melodear.desktop.keyboard.KeyboardSettingsView;

public class SettingsController {

	private static Logger logger = LogManager.getLogger(SettingsController.class);

	private AllSettings settings;
	private AllSettings settingsNew;

	@FXML
	DialogPane settingsDlg;

	@FXML
	BorderPane settingsPane;

	@FXML
	ListView<SettingsView<?>> sectionSelect;

	public SettingsController(AllSettings settings) {
		logger.debug("Creating SettingsController");
		this.settings = settings;
		this.settingsNew = settings.copy();
	}

	public void initialize() {
		ObservableList<SettingsView<?>> sectionsObs
				= FXCollections.observableArrayList(buildSections());
		sectionSelect.setCellFactory(v -> new ListCell<SettingsView<?>>() {
			@Override
			protected void updateItem(SettingsView<?> item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null) {
					setText(item.getName());
				} else {
					setText("");
				}
			}
		});
		sectionSelect.setItems(sectionsObs);
		sectionSelect.getSelectionModel().selectedItemProperty()
				.addListener((v, o, n) -> {
					logger.debug("Selected section: {}", n);
					settingsPane.setCenter(n.getNode(settingsNew));
				});
		sectionSelect.getSelectionModel().select(0);

		EventHandler<? super ActionEvent> updater
				= e -> {
					settings.updateWith(settingsNew);
					userPrefs().writeSettings(settings);
				};
		Button applyBtn = (Button) settingsDlg.lookupButton(ButtonType.APPLY);
		applyBtn.addEventHandler(ActionEvent.ACTION, updater);
	}

	private List<SettingsView<?>> buildSections() {
		List<SettingsView<?>> sections = new ArrayList<>();
		sections.add(new KeyboardSettingsView());
		sections.add(new MidiSettingsView());
		return sections;
	}

	private PreferencesStorage userPrefs() {
		return new PreferencesStorage(
				Preferences.userNodeForPackage(Main.class));
	}
}
