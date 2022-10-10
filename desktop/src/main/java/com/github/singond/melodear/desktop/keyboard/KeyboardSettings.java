package com.github.singond.melodear.desktop.keyboard;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.settings.PropertyAbstractSettingsTree;

public class KeyboardSettings
		extends PropertyAbstractSettingsTree<KeyboardSettings> {

	private static Logger logger = LogManager.getLogger(KeyboardSettings.class);

	/**
	 * Duration of the note played after a key is pressed.
	 */
	private final Property<KeyPlayDuration> keyDuration;

	/**
	 * Format of piano key label.
	 */
	private ObjectProperty<NamedKeyLabelFormat> keyLabelFormat
			= new SimpleObjectProperty<>(NamedKeyLabelFormats.getDefaultFormat());

	public KeyboardSettings(String name) {
		super(name);
		logger.debug("Creating KeyboardSettings");
		keyDuration = newPropertyNode("keyDuration", KeyPlayDuration.KEY_HELD);
		keyLabelFormat = newPropertyNode("keyLabelFormat",
				new SimpleObjectProperty<>(NamedKeyLabelFormats.getDefaultFormat()),
				NamedKeyLabelFormats.STRING_CONVERTER);
		keyDuration.addListener((v, o, n) ->
				logger.debug("Key duration changed from {} to {}", o, n));
	}

	public KeyboardSettings() {
		this(KeyboardSettings.class.getName());
	}

	@Override
	protected KeyboardSettings newInstance(String key) {
		return new KeyboardSettings(key);
	}

	public KeyPlayDuration getKeyDuration() {
		return keyDuration.getValue();
	}

	public void setKeyDuration(KeyPlayDuration keyDuration) {
		this.keyDuration.setValue(keyDuration);
	}

	public Property<KeyPlayDuration> keyDurationProperty() {
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

}
