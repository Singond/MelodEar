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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MidiSettingsController {

	private static Logger logger = LogManager.getLogger(MidiSettingsController.class);

	private final MidiSettings settings;

	@FXML
	TableView<Synthesizer> synth;
	@FXML
	TableColumn<Synthesizer, String> synthNameColumn;
	@FXML
	TableColumn<Synthesizer, String> synthVendorColumn;
	@FXML
	TableColumn<Synthesizer, String> synthDescriptionColumn;
	@FXML
	TableColumn<Synthesizer, String> synthVersionColumn;

	@FXML
	ChoiceBox<Synthesizer> synth2;

	public MidiSettingsController(MidiSettings settings) {
		logger.debug("Creating MidiSettingsController");
		this.settings = settings;
	}

	public void initialize() {
		logger.debug("Initializing synthesizer list");

		// Synthesizer
		settings.synthProperty().bind(
				synth.getSelectionModel().selectedItemProperty());
		synth.setItems(FXCollections.observableArrayList(synthesizers()));
		try (Synthesizer currentSynth = MidiSystem.getSynthesizer()) {
			synth.getSelectionModel().select(currentSynth);
		} catch (MidiUnavailableException e) {
			logger.error("Could not obtain current synthesizer", e);
		}
		synthNameColumn.setCellValueFactory(f -> new SimpleStringProperty(
				f.getValue().getDeviceInfo().getName()));
		synthVendorColumn.setCellValueFactory(f -> new SimpleStringProperty(
				f.getValue().getDeviceInfo().getVendor()));
		synthDescriptionColumn.setCellValueFactory(f -> new SimpleStringProperty(
				f.getValue().getDeviceInfo().getDescription()));
		synthVersionColumn.setCellValueFactory(f -> new SimpleStringProperty(
				f.getValue().getDeviceInfo().getVersion()));
	}

	private List<Synthesizer> synthesizers() {
		List<Synthesizer> result = new ArrayList<>();
		for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
			logger.debug("MIDI device: {}", info);
			try (MidiDevice device = MidiSystem.getMidiDevice(info)) {
				if (device instanceof Synthesizer) {
					result.add((Synthesizer) device);
				}
			} catch (MidiUnavailableException e) {
				logger.error("Could not obtain device " + info, e);
			}
		}
		return result;
	}

}
