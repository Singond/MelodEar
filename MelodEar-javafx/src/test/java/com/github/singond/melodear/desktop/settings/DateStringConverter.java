package com.github.singond.melodear.desktop.settings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.util.StringConverter;

public class DateStringConverter extends StringConverter<Date> {

	private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public String toString(Date date) {
		return FORMAT.format(date);
	}

	@Override
	public Date fromString(String string) {
		try {
			return FORMAT.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
