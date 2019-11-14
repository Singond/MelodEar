package com.github.singond.melodear.desktop.keyboard;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.settings.EnumStringConverter;
import com.github.singond.melodear.desktop.settings.PropertyAbstractSettingsTree;

public class KeyboardSettings
		extends PropertyAbstractSettingsTree<KeyboardSettings> {

	private static Logger logger = LogManager.getLogger(KeyboardSettings.class);

	/**
	 * Duration of the note played after a key is pressed.
	 */
	private final ObjectProperty<KeyPlayDuration> keyDuration;

	/**
	 * Format of piano key label.
	 */
	private ObjectProperty<NamedKeyLabelFormat> keyLabelFormat
			= new SimpleObjectProperty<>(KeyLabelFormats.getDefaultFormat());

	public KeyboardSettings() {
		super(KeyboardSettings.class.getName());
		logger.debug("Creating KeyboardSettings");
		keyDuration = newPropertyNode("keyDuration",
				new SimpleObjectProperty<>(KeyPlayDuration.KEY_HELD),
				new EnumStringConverter<>(KeyPlayDuration.KEY_HELD));
		keyLabelFormat = newPropertyNode("keyLabelFormat",
				new SimpleObjectProperty<>(KeyLabelFormats.getDefaultFormat()));
		keyDuration.addListener((v, o, n) ->
				logger.debug("Key duration changed from {} to {}", o, n));
	}

	@Override
	protected KeyboardSettings newInstance() {
		return new KeyboardSettings();
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

}
