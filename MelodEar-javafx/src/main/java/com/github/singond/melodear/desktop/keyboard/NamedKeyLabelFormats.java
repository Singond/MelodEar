package com.github.singond.melodear.desktop.keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.util.StringConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.music.text.PitchFormat;
import com.github.singond.music.text.PitchFormats;

class NamedKeyLabelFormats {

	private static Logger logger
			= LogManager.getLogger(NamedKeyLabelFormats.class);

	static final NamedKeyLabelFormat SCIENTIFIC_UNICODE;

//	static final KeyLabelFormat SCIENTIFIC_ASCII;

	static final NamedKeyLabelFormat HELMHOLTZ_H_UNICODE;

	static final NamedKeyLabelFormat HELMHOLTZ_H_ASCII;

	static final NamedKeyLabelFormat HELMHOLTZ_B_UNICODE;

	static final NamedKeyLabelFormat HELMHOLTZ_B_ASCII;

	private static final Map<String, NamedKeyLabelFormat> FORMATS;
	private static final List<NamedKeyLabelFormat> FORMAT_LIST;

	static final Converter STRING_CONVERTER = new Converter() {
		@Override
		public String toString(NamedKeyLabelFormat fmt) {
			return fmt.getKey();
		}

		@Override
		public NamedKeyLabelFormat fromString(String string) {
			NamedKeyLabelFormat fmt = NamedKeyLabelFormats.getFormat(string);
			if (fmt == null) {
				logger.warn("No key label format matches string '{}'", string);
			}
			return fmt;
		}
	};

	static final Converter LABEL_CONVERTER = new Converter() {
		@Override
		public String toString(NamedKeyLabelFormat fmt) {
			return fmt.getName();
		}

		@Override
		public NamedKeyLabelFormat fromString(String string) {
			throw new UnsupportedOperationException(
					"Cannot convert to label format from name");
		}
	};

	static {
		Map<String, NamedKeyLabelFormat> fmts = new LinkedHashMap<>();
		SCIENTIFIC_UNICODE = initFmt("scientific_unicode",
				PitchFormats.SCIENTIFIC, fmts);
		HELMHOLTZ_H_UNICODE = initFmt("helmholtz_h_unicode",
				PitchFormats.HELMHOLTZ_H_UNICODE, fmts);
		HELMHOLTZ_H_ASCII = initFmt("helmholtz_h_ascii",
				PitchFormats.HELMHOLTZ_H_ASCII, fmts);
		HELMHOLTZ_B_UNICODE = initFmt("helmholtz_b_unicode",
				PitchFormats.HELMHOLTZ_B_UNICODE, fmts);
		HELMHOLTZ_B_ASCII = initFmt("helmholtz_b_ascii",
				PitchFormats.HELMHOLTZ_B_ASCII, fmts);
		FORMATS = Collections.unmodifiableMap(fmts);
		List<NamedKeyLabelFormat> fmtList = new ArrayList<>(5);
		fmtList.addAll(FORMATS.values());
		FORMAT_LIST = Collections.unmodifiableList(fmtList);
	}

	private static NamedKeyLabelFormat initFmt(String key, PitchFormat fmt,
			Map<String, NamedKeyLabelFormat> map) {
		PredefKeyLabelFormat keyfmt = new PredefKeyLabelFormat(key, fmt);
		map.put(key, keyfmt);
		return keyfmt;
	}

	public static List<NamedKeyLabelFormat> getFormats() {
		return FORMAT_LIST;
	}

	public static NamedKeyLabelFormat getDefaultFormat() {
		return SCIENTIFIC_UNICODE;
	}

	public static NamedKeyLabelFormat getFormat(String key) {
		return FORMATS.get(key);
	}

	private static abstract class Converter
			extends StringConverter<NamedKeyLabelFormat> {

		private static Logger logger
				= LogManager.getLogger(NamedKeyLabelFormats.class);

		@Override
		public NamedKeyLabelFormat fromString(String string) {
			NamedKeyLabelFormat fmt = NamedKeyLabelFormats.getFormat(string);
			if (fmt == null) {
				logger.warn("No key label format matches string '{}'", string);
			}
			return fmt;
		}
	}

}
