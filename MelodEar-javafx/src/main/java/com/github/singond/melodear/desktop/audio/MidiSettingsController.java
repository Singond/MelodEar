package com.github.singond.melodear.desktop.audio;

import javax.sound.midi.MidiDevice;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MidiSettingsController {

	private static Logger logger
			= LogManager.getLogger(MidiSettingsController.class);

	private final MidiSettings settings;

	@FXML
	TableView<MidiDevice.Info> synth;
	@FXML
	TableColumn<MidiDevice.Info, String> synthNameColumn;
	@FXML
	TableColumn<MidiDevice.Info, String> synthVendorColumn;
	@FXML
	TableColumn<MidiDevice.Info, String> synthDescriptionColumn;
	@FXML
	TableColumn<MidiDevice.Info, String> synthVersionColumn;

	@FXML
	TextField soundbank;

	public MidiSettingsController(MidiSettings settings) {
		logger.debug("Creating MidiSettingsController");
		this.settings = settings;
	}

	public void initialize() {
		logger.debug("Initializing MIDI settings");
		initSynth();
	}

	private void initSynth() {
		logger.debug("Initializing synthesizer list");
		synth.setItems(settings.getAvailableSynthesizers());
		synth.getSelectionModel().select(settings.getSynth());
		settings.synthProperty().bind(
				synth.getSelectionModel().selectedItemProperty());

		// Set column values
		synthNameColumn.setCellValueFactory(f -> new SimpleStringProperty(
				f.getValue().getName()));
		synthVendorColumn.setCellValueFactory(f -> new SimpleStringProperty(
				f.getValue().getVendor()));
		synthDescriptionColumn.setCellValueFactory(f -> new SimpleStringProperty(
				f.getValue().getDescription()));
		synthVersionColumn.setCellValueFactory(f -> new SimpleStringProperty(
				f.getValue().getVersion()));
	}

}
