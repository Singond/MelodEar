package com.github.singond.melodear.desktop.keyboard;

import java.util.ResourceBundle;

import com.github.singond.music.Pitch;
import com.github.singond.music.text.PitchFormat;

public class PredefKeyLabelFormat implements KeyLabelFormat {

	private static final ResourceBundle bundle
			= ResourceBundle.getBundle("loc/settings");

	private final String name;
	private final PitchFormat format;

	public PredefKeyLabelFormat(String name, PitchFormat format) {
//		this.name = bundle.getString(name);
		this.name = name;
		this.format = format;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String formatLabel(Pitch pitch) {
		return format.format(pitch).toString();
	}

}
