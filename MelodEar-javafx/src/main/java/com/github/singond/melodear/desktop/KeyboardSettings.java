package com.github.singond.melodear.desktop;

import javax.inject.Inject;
import javax.inject.Singleton;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Singleton
public class KeyboardSettings {

	private static Logger logger = LogManager.getLogger(KeyboardSettings.class);

	private ObjectProperty<KeyPlayDuration> keyDuration
			= new SimpleObjectProperty<>(KeyPlayDuration.KEY_HELD);

	@Inject
	public KeyboardSettings() {
		logger.debug("Creating KeyboardSettings");
		keyDuration.addListener((v, o, n) ->
				logger.debug("Key duration changed from {} to {}", o, n));
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

}
