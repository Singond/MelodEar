package com.github.singond.melodear.desktop.settings;

import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.audio.MidiSettings;
import com.github.singond.melodear.desktop.keyboard.KeyboardSettings;
import com.github.singond.settings.AbstractSettingsTree;

/**
 * Root container of application settings.
 *
 * @author Singon
 */
@Singleton
public class AllSettings extends AbstractSettingsTree<AllSettings>{

	private static Logger logger = LogManager.getLogger(AllSettings.class);

	private final KeyboardSettings keyboard;
	private final MidiSettings midi;

	/**
	 * Constructs a new instance of MelodEar settings.
	 *
	 * @param key the node key in the settings tree
	 */
	public AllSettings(String key) {
		super(key);
		logger.debug("Creating AllSettings");
		keyboard = newNode(new KeyboardSettings("keyboard"));
		midi     = newNode(new MidiSettings("midi"));
	}

	@Override
	protected AllSettings newInstance(String key) {
		return new AllSettings(key);
	}

	/**
	 * Returns the piano keyboard settings.
	 *
	 * @return shared settings for the piano keyboard
	 */
	public KeyboardSettings keyboard() {
		return keyboard;
	}

	/**
	 * Returns the MIDI settings.
	 *
	 * @return MIDI system settings
	 */
	public MidiSettings midi() {
		return midi;
	}
}
