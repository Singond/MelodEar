package com.github.singond.melodear.desktop.settings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import com.github.singond.settings.AbstractSettingsValue;

public class DatePropertySettingsValue
		extends AbstractSettingsValue<Property<Date>, DatePropertySettingsValue> {

	private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	protected DatePropertySettingsValue(String key, Property<Date> value) {
		super(key, value);
	}

	@Override
	public Property<Date> valueCopy() {
		return new SimpleObjectProperty<Date>(
				new Date(value().getValue().getTime()));
	}

	@Override
	public String valueToString() {
		return FORMAT.format(value().getValue());
	}

	@Override
	public Property<Date> valueFromString(String string) {
		try {
			return new SimpleObjectProperty<Date>(FORMAT.parse(string));
		} catch (ParseException e) {
			e.printStackTrace();
			return new SimpleObjectProperty<Date>(null);
		}
	}
}
