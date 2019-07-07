package com.github.singond.melodear.desktop.audio;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
		settings.synthProperty().bind(
				synth.getSelectionModel().selectedItemProperty());
		List<MidiDevice.Info> synths = synthesizers();
		synth.setItems(FXCollections.observableArrayList(synths));

		// Select current synthesizer
		try (Synthesizer currentSynth = MidiSystem.getSynthesizer()) {
			if (!synths.contains(currentSynth.getDeviceInfo())) {
				logger.warn("List of synths does not contain the current one");
			}
			synth.getSelectionModel().select(currentSynth.getDeviceInfo());
		} catch (MidiUnavailableException e) {
			logger.error("Could not obtain current synthesizer", e);
		}

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

	private List<MidiDevice.Info> synthesizers() {
		List<MidiDevice.Info> result = new ArrayList<>();
		for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
			logger.debug("MIDI device: {}", info);
			try (MidiDevice device = MidiSystem.getMidiDevice(info)) {
				if (device instanceof Synthesizer) {
					result.add(info);
				}
			} catch (MidiUnavailableException e) {
				logger.error("Could not obtain device " + info, e);
			}
		}
		return result;
	}

}
