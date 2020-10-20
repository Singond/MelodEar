package com.github.singond.settings;

/**
 * Signals that an exception has occured in the settings storage.
 */
public class SettingsStorageException extends Exception {

	private static final long serialVersionUID = 1L;

	public SettingsStorageException() {
		super();
	}

	public SettingsStorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public SettingsStorageException(String message) {
		super(message);
	}

	public SettingsStorageException(Throwable cause) {
		super(cause);
	}
}
