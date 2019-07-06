package com.github.singond.melodear.desktop.components;

import com.github.singond.music.Pitch;

public interface KeyLabelFormat {

	/**
	 * Returns a label for a key of the given pitch.
	 *
	 * @param pitch pitch of the key
	 * @return text label of key with pitch {@code pitch}
	 */
	String formatLabel(Pitch pitch);

}