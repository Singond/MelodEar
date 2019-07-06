package com.github.singond.melodear.desktop.keyboard;

import javafx.util.StringConverter;

import com.github.singond.melodear.desktop.components.KeyLabelFormat;

public interface NamedKeyLabelFormat extends KeyLabelFormat {

	/**
	 * Returns a user-friendly name of this label format provider.
	 *
	 * @return the name of this format to be displayed to users
	 */
	String getName();

	public static class Converter extends StringConverter<NamedKeyLabelFormat> {

		@Override
		public String toString(NamedKeyLabelFormat obj) {
			return obj.getName();
		}

		@Override
		public NamedKeyLabelFormat fromString(String string) {
			// TODO Will not be called?
			throw new UnsupportedOperationException();
		}

	}
}