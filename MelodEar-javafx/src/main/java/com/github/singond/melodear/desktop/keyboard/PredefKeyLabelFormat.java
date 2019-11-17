package com.github.singond.melodear.desktop.keyboard;

import java.util.ResourceBundle;

import com.github.singond.music.Pitch;
import com.github.singond.music.text.PitchFormat;

public class PredefKeyLabelFormat implements NamedKeyLabelFormat {

	private static final ResourceBundle bundle
			= ResourceBundle.getBundle("loc/settings");

	private final String key;
	private final String name;
	private final PitchFormat format;

	public PredefKeyLabelFormat(String key, PitchFormat format) {
		this.key = key;
		this.name = bundle.getString("keyboard.key_label_format." + key);
		this.format = format;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String formatLabel(Pitch pitch) {
		return format.format(pitch).toString();
	}

	@Override
	public String toString() {
		return String.format("%s ('%s')", format, name);
	}

}
