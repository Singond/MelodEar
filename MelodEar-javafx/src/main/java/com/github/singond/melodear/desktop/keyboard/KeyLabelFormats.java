package com.github.singond.melodear.desktop.keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.singond.music.text.PitchFormats;

class KeyLabelFormats {

	static final KeyLabelFormat SCIENTIFIC_UNICODE = new PredefKeyLabelFormat(
			"scientific_unicode", PitchFormats.SCIENTIFIC);

//	static final KeyLabelFormat SCIENTIFIC_ASCII = new PredefKeyLabelFormat(
//			"scientific_ascii", PitchFormats.SCIENTIFIC_ASCII);

	static final KeyLabelFormat HELMHOLTZ_H_UNICODE = new PredefKeyLabelFormat(
			"helmholtz_h_unicode", PitchFormats.HELMHOLTZ_H_UNICODE);

	static final KeyLabelFormat HELMHOLTZ_H_ASCII = new PredefKeyLabelFormat(
			"helmholtz_h_ascii", PitchFormats.HELMHOLTZ_H_ASCII);

	static final KeyLabelFormat HELMHOLTZ_B_UNICODE = new PredefKeyLabelFormat(
			"helmholtz_b_unicode", PitchFormats.HELMHOLTZ_B_UNICODE);

	static final KeyLabelFormat HELMHOLTZ_B_ASCII = new PredefKeyLabelFormat(
			"helmholtz_b_ascii", PitchFormats.HELMHOLTZ_B_ASCII);

	private static final List<KeyLabelFormat> FORMATS;

	static {
		List<KeyLabelFormat> fmts = new ArrayList<>(5);
		fmts.add(SCIENTIFIC_UNICODE);
		fmts.add(HELMHOLTZ_H_UNICODE);
		fmts.add(HELMHOLTZ_H_ASCII);
		fmts.add(HELMHOLTZ_B_UNICODE);
		fmts.add(HELMHOLTZ_B_ASCII);
		FORMATS = Collections.unmodifiableList(fmts);
	}

	public static List<KeyLabelFormat> getFormats() {
		return FORMATS;
	}

	public static KeyLabelFormat getDefaultFormat() {
		return SCIENTIFIC_UNICODE;
	}
}
