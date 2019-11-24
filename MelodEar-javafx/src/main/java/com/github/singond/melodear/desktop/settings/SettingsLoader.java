package com.github.singond.melodear.desktop.settings;

import java.util.prefs.Preferences;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.Main;

/**
 * A provider object for MelodEar settings which enables persisting
 * between sessions.
 *
 * @author Singon
 */
public final class SettingsLoader {

	private static Logger logger = LogManager.getLogger(SettingsLoader.class);

	private final AllSettings settings;

	public SettingsLoader() {
		logger.debug("Initializing SettingsLoader");
		settings = new AllSettings("melodear");
		loadSettings();
	}

	public AllSettings getSettings() {
		return settings;
	}

	private PreferencesStorage userPrefs() {
		return new PreferencesStorage(
				Preferences.userNodeForPackage(Main.class));
	}

	public void loadSettings() {
		logger.debug("Loading settings from user preferences");
		userPrefs().readSettings(settings);
	}

	public void writeSettings() {
		logger.debug("Writing settings to user preferences");
		userPrefs().writeSettings(settings);
	}

	public void updateSettingsWith(AllSettings settings) {
		this.settings.updateWith(settings);
		writeSettings();
	}
}
