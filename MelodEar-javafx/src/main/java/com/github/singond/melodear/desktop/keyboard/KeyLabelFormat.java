package com.github.singond.melodear.desktop.keyboard;

import javafx.util.StringConverter;

import com.github.singond.music.Pitch;

interface KeyLabelFormat {

	/**
	 * Returns a user-friendly name of this label format provider.
	 *
	 * @return the name of this format to be displayed to users
	 */
	String getName();

	/**
	 * Returns a label for a key of the given pitch.
	 *
	 * @param pitch pitch of the key
	 * @return text label of key with pitch {@code pitch}
	 */
	String formatLabel(Pitch pitch);

	public static class Converter extends StringConverter<KeyLabelFormat> {

		@Override
		public String toString(KeyLabelFormat obj) {
			return obj.getName();
		}

		@Override
		public KeyLabelFormat fromString(String string) {
			// TODO Will not be called?
			throw new UnsupportedOperationException();
		}

	}
}