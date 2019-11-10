package com.github.singond.melodear.desktop.keyboard;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.settings.SettingsTree;

public class KeyboardSettings implements SettingsTree<KeyboardSettings> {

	private static Logger logger = LogManager.getLogger(KeyboardSettings.class);

	/**
	 * Duration of the note played after a key is pressed.
	 */
	private ObjectProperty<KeyPlayDuration> keyDuration
			= new SimpleObjectProperty<>(KeyPlayDuration.KEY_HELD);

	/**
	 * Format of piano key label.
	 */
	private ObjectProperty<NamedKeyLabelFormat> keyLabelFormat
			= new SimpleObjectProperty<>(KeyLabelFormats.getDefaultFormat());

	public KeyboardSettings() {
		logger.debug("Creating KeyboardSettings");
		keyDuration.addListener((v, o, n) ->
				logger.debug("Key duration changed from {} to {}", o, n));
	}

	private KeyboardSettings(KeyboardSettings src) {
		updateFields(src, this);
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

	public NamedKeyLabelFormat getKeyLabelFormat() {
		return keyLabelFormat.get();
	}

	public void setKeyLabelFormat(NamedKeyLabelFormat keyLabelFormat) {
		this.keyLabelFormat.set(keyLabelFormat);
	}

	public ObjectProperty<NamedKeyLabelFormat> keyLabelFormatProperty() {
		return keyLabelFormat;
	}

	private static void updateFields(KeyboardSettings src, KeyboardSettings tgt) {
		tgt.keyDuration.set(src.keyDuration.get());
		tgt.keyLabelFormat.set(src.keyLabelFormat.get());
	}

	@Override
	public KeyboardSettings copy() {
		return new KeyboardSettings(this);
	}

	@Override
	public void updateFrom(KeyboardSettings src) {
		updateFields(src, this);
	}
}
