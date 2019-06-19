package com.github.singond.melodear.desktop;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.keyboard.KeyboardSettings;

/**
 * Root container of application settings.
 *
 * @author Singon
 */
@Singleton
public class Settings {

	private static Logger logger = LogManager.getLogger(Settings.class);

	private final KeyboardSettings keyboard;

	@Inject
	public Settings() {
		logger.debug("Creating Settings");
		keyboard = new KeyboardSettings();
	}

	/**
	 * A copy constructor.
	 *
	 * @param src source object
	 */
	public Settings(Settings src) {
		keyboard = new KeyboardSettings(src.keyboard);
	}

	public void updateFrom(Settings src) {
		logger.debug("Updating settings from {}", src);
		keyboard.updateFrom(src.keyboard);
	}

	/**
	 * Returns the piano keyboard settings.
	 *
	 * @return shared settings for the piano keyboard
	 */
	public KeyboardSettings keyboard() {
		return keyboard;
	}

}
