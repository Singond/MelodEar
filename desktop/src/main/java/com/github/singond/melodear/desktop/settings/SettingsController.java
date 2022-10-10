package com.github.singond.melodear.desktop.settings;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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

import com.github.singond.melodear.desktop.audio.MidiSettingsView;
import com.github.singond.melodear.desktop.keyboard.KeyboardSettingsView;
import com.github.singond.melodear.desktop.trainer.MelodyTrainerSettingsView;

/**
 * JavaFX controller class for the settings dialog window.
 *
 * @author Singon
 */
public class SettingsController {

	private static Logger logger = LogManager.getLogger(SettingsController.class);

	private SettingsLoader settingsLoader;
	private AllSettings settingsNew;

	@Inject
	KeyboardSettingsView keyboardSettingsView;
	@Inject
	MidiSettingsView midiSettingsView;
	@Inject
	MelodyTrainerSettingsView melodyTrainerSettingsView;

	@FXML
	DialogPane settingsDlg;

	@FXML
	BorderPane settingsPane;

	@FXML
	ListView<SettingsView<?>> sectionSelect;

	/**
	 * Creates a new settings controller using a copy of the current settings.
	 * These settings are copied back to the global instance of settings
	 * if the "Apply" button is pressed.
	 *
	 * @param settingsLoader object providing current settings
	 */
	@Inject
	public SettingsController(SettingsLoader settingsLoader) {
		logger.debug("Creating SettingsController");
		this.settingsLoader = settingsLoader;
		this.settingsNew = settingsLoader.getSettings().copy();
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
				= e -> settingsLoader.updateSettingsWith(settingsNew);
		Button applyBtn = (Button) settingsDlg.lookupButton(ButtonType.APPLY);
		applyBtn.addEventHandler(ActionEvent.ACTION, updater);
	}

	private List<SettingsView<?>> buildSections() {
		List<SettingsView<?>> sections = new ArrayList<>();
		sections.add(keyboardSettingsView);
		sections.add(midiSettingsView);
		sections.add(melodyTrainerSettingsView);
		return sections;
	}
}
