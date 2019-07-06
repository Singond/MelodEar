package com.github.singond.melodear.desktop.audio;

import javax.sound.midi.Synthesizer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.settings.Settings;

public class MidiSettings implements Settings {

	private static Logger logger = LogManager.getLogger(MidiSettings.class);

	/**
	 * MIDI synthesizer to be used.
	 */
	private ObjectProperty<Synthesizer> synth = new SimpleObjectProperty<>();

	public MidiSettings() {
		logger.debug("Creating MidiSettings");
		synth.addListener((v, o, n) ->
				logger.debug("Synthesizer changed from {} to {}", o, n));
	}

	public MidiSettings(MidiSettings src) {
		copyFields(src, this);
	}

	public Synthesizer getSynth() {
		return synth.get();
	}

	public void setSynth(Synthesizer synth) {
		this.synth.set(synth);
	}

	public ObjectProperty<Synthesizer> synthProperty() {
		return synth;
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
