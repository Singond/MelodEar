package com.github.singond.melodear.desktop.settings;

import java.util.prefs.Preferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.Main;
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

	/**
	 * Constructs a new instance of MelodEar settings with the default
	 * key and initializes it using user preferences.
	 * <p>
	 * This constructor is meant to be used in application initialization.
	 */
	@Inject
	public AllSettings() {
		this("melodear");
		loadFromUserPreferences();
	}

	@Override
	protected AllSettings newInstance(String key) {
		return new AllSettings();
	}

	public void loadFromUserPreferences() {
		PreferencesStorage prefs = new PreferencesStorage(
				Preferences.userNodeForPackage(Main.class));
		prefs.readSettings(this);
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
