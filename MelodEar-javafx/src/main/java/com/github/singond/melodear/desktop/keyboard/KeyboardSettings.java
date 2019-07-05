package com.github.singond.melodear.desktop.keyboard;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.settings.Settings;

public class KeyboardSettings implements Settings {

	private static Logger logger = LogManager.getLogger(KeyboardSettings.class);

	/**
	 * Duration of the note played after a key is pressed.
	 */
	private ObjectProperty<KeyPlayDuration> keyDuration
			= new SimpleObjectProperty<>(KeyPlayDuration.KEY_HELD);

	/**
	 * Format of piano key label.
	 */
	private ObjectProperty<KeyLabelFormat> keyLabelFormat
			= new SimpleObjectProperty<>(KeyLabelFormats.getDefaultFormat());

	public KeyboardSettings() {
		logger.debug("Creating KeyboardSettings");
		keyDuration.addListener((v, o, n) ->
				logger.debug("Key duration changed from {} to {}", o, n));
	}

	public KeyboardSettings(KeyboardSettings src) {
		copyFields(src, this);
	}

	public KeyPlayDuration getKeyDuration() {
		return keyDuration.get();
	}

	public void setKeyDuration(KeyPlayDuration keyDuration) {
		this.keyDuration.set(keyDuration);
	}

	public ObjectProperty<KeyPlayDuration> keyDurationProperty() {
		return keyDuration;
	}

	public KeyLabelFormat getKeyLabelFormat() {
		return keyLabelFormat.get();
	}

	public void setKeyLabelFormat(KeyLabelFormat keyLabelFormat) {
		this.keyLabelFormat.set(keyLabelFormat);
	}

	public ObjectProperty<KeyLabelFormat> keyLabelFormatProperty() {
		return keyLabelFormat;
	}

	private static void copyFields(KeyboardSettings src, KeyboardSettings tgt) {
		tgt.keyDuration = new SimpleObjectProperty<>(src.keyDuration.get());
		tgt.keyLabelFormat = new SimpleObjectProperty<>(src.keyLabelFormat.get());
	}

	public void updateFrom(KeyboardSettings src) {
		copyFields(src, this);
	}
}
