package com.github.singond.melodear.desktop.keyboard;

import com.github.singond.melodear.desktop.components.KeyLabelFormat;

public interface NamedKeyLabelFormat extends KeyLabelFormat {

	/**
	 * Returns a key of this format.
	 *
	 * @return a key defining this format
	 */
	String getKey();

	/**
	 * Returns a user-friendly name of this label format provider.
	 *
	 * @return the name of this format to be displayed to users
	 */
	String getName();

}