package com.github.singond.melodear.desktop.audio;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MidiSettingsController {

	private static Logger logger = LogManager.getLogger(MidiSettingsController.class);

	private final MidiSettings settings;

	@FXML
	ChoiceBox<Synthesizer> synth;

	public MidiSettingsController(MidiSettings settings) {
		logger.debug("Creating MidiSettingsController");
		this.settings = settings;
	}

	public void initialize() {
		logger.debug("Initializing synthesizer list");

		// Synthesizer
		// TODO: Select current synth
//		synths.getSelectionModel().select(currentSynth);
		settings.synthProperty().bind(
				synth.getSelectionModel().selectedItemProperty());
		synth.setItems(FXCollections.observableArrayList(synthesizers()));
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
