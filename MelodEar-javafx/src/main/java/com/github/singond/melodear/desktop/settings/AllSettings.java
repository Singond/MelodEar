package com.github.singond.melodear.desktop.settings;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.audio.MidiSettings;
import com.github.singond.melodear.desktop.keyboard.KeyboardSettings;

/**
 * Root container of application settings.
 *
 * @author Singon
 */
@Singleton
public class AllSettings {

	private static Logger logger = LogManager.getLogger(AllSettings.class);

	private final KeyboardSettings keyboard;

	private final MidiSettings midi;

	@Inject
	public AllSettings() {
		logger.debug("Creating AllSettings");
		keyboard = new KeyboardSettings();
		midi = new MidiSettings();
	}

	/**
	 * A copy constructor.
	 *
	 * @param src source object
	 */
	public AllSettings(AllSettings src) {
		keyboard = new KeyboardSettings(src.keyboard);
		midi = new MidiSettings(src.midi);
	}

	public void updateFrom(AllSettings src) {
		logger.debug("Updating settings from {}", src);
		keyboard.updateFrom(src.keyboard);
		midi.updateFrom(src.midi);
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
