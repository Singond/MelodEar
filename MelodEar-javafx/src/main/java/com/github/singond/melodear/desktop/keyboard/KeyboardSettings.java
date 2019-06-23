package com.github.singond.melodear.desktop.keyboard;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.settings.Settings;

public class KeyboardSettings implements Settings {

	private static Logger logger = LogManager.getLogger(KeyboardSettings.class);

	private ObjectProperty<KeyPlayDuration> keyDuration
			= new SimpleObjectProperty<>(KeyPlayDuration.KEY_HELD);

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

	private static void copyFields(KeyboardSettings src, KeyboardSettings tgt) {
		tgt.keyDuration = new SimpleObjectProperty<>(src.keyDuration.get());
	}

	public void updateFrom(KeyboardSettings src) {
		copyFields(src, this);
	}
}
