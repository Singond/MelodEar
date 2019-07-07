package com.github.singond.melodear.desktop.audio;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.settings.Settings;

public final class MidiSettings implements Settings {

	private static Logger logger = LogManager.getLogger(MidiSettings.class);

	/**
	 * MIDI synthesizer to be used.
	 */
	private ObjectProperty<MidiDevice.Info> synth = new SimpleObjectProperty<>();

	public MidiSettings() {
		logger.debug("Creating MidiSettings");
		initSynth();
		synth.addListener((v, o, n) ->
				logger.debug("Synthesizer changed from {} to {}", o, n));
	}

	public MidiSettings(MidiSettings src) {
		copyFields(src, this);
	}

	public MidiDevice.Info getSynth() {
		return synth.get();
	}

	public void setSynth(MidiDevice.Info synth) {
		this.synth.set(synth);
	}

	public ObjectProperty<MidiDevice.Info> synthProperty() {
		return synth;
	}

	public final ObservableList<MidiDevice.Info> getAvailableSynthesizers() {
		return FXCollections.observableArrayList(synthesizers());
	}

	private void initSynth() {
		logger.debug("Initializing synthesizer settings");

		// Detect current synthesizer
		List<MidiDevice.Info> synths = synthesizers();
		try (Synthesizer currentSynth = MidiSystem.getSynthesizer()) {
			if (synths.contains(currentSynth.getDeviceInfo())) {
				synth.set(currentSynth.getDeviceInfo());
			} else {
				logger.warn("Cannot find current synthesizer ({}) among"
						+ "available devices ({})", currentSynth, synths);
			}
		} catch (MidiUnavailableException e) {
			logger.error("Could not obtain current synthesizer", e);
		}
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

	private static void copyFields(MidiSettings src, MidiSettings tgt) {
		tgt.synth = new SimpleObjectProperty<>(src.synth.get());
	}

	private static void updateFields(MidiSettings src, MidiSettings tgt) {
		tgt.synth.set(src.synth.get());
	}

	public void updateFrom(MidiSettings src) {
		updateFields(src, this);
	}

}
