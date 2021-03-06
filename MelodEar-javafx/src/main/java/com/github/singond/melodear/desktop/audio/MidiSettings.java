package com.github.singond.melodear.desktop.audio;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.settings.PropertyAbstractSettingsTree;

public final class MidiSettings
		extends PropertyAbstractSettingsTree<MidiSettings> {

	private static Logger logger = LogManager.getLogger(MidiSettings.class);

	/**
	 * MIDI synthesizer to be used.
	 */
	private ObjectProperty<MidiDevice.Info> synth = new SimpleObjectProperty<>();

	/**
	 * Soundbank used in the MIDI device to produce sound.
	 */
	private Property<Path> soundbank;

	/**
	 * The default directory displayed by the file chooser when prompting
	 * to select a sound bank.
	 */
	private Property<Path> soundbankDefaultDir;

	public MidiSettings(String key) {
		super(key);
		logger.debug("Creating MidiSettings");
		synth = newPropertyNode("synth", new SimpleObjectProperty<>());
		soundbank = newPropertyNode("soundbank", (Path) null);
		soundbankDefaultDir = newPropertyNode("soundbankDefaultDir", (Path) null);
		initSynth();
		synth.addListener((v, o, n) ->
				logger.debug("Synthesizer changed from {} to {}", o, n));
	}

	public MidiSettings() {
		this(MidiSettings.class.getName());
	}

	@Override
	protected MidiSettings newInstance(String key) {
		return new MidiSettings(key);
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

	public Path getSoundbank() {
		return soundbank.getValue();
	}

	public void setSoundbank(Path soundbank) {
		this.soundbank.setValue(soundbank);
	}

	public Property<Path> soundbankProperty() {
		return soundbank;
	}

	public Path getSoundbankDefaultDir() {
		return soundbankDefaultDir.getValue();
	}

	public void setSoundbankDefaultDir(Path soundbankDefaultDir) {
		this.soundbankDefaultDir.setValue(soundbankDefaultDir);
	}

	public Property<Path> soundbankDefaultDirProperty() {
		return this.soundbankDefaultDir;
	}

}
