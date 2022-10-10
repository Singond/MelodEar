package com.github.singond.settings;

public interface SettingsStorage {

	/**
	 * Writes (saves) the given settings tree into the storage.
	 *
	 * @param settings the settings to be saved
	 * @throws SettingsStorageException if an error occurs while
	 *         writing the settings
	 */
	void writeSettings(SettingsTreeNode<?> settings)
			throws SettingsStorageException;

	/**
	 * Reads the settings for the given object from the storage
	 * and applies them to the object.
	 *
	 * @param settings the settings tree to be set with values read
	 *        from storage
	 * @throws SettingsStorageException if an error occurs while
	 *         reading the settings
	 */
	void readSettings(SettingsTreeNode<?> settings)
			 throws SettingsStorageException;

}