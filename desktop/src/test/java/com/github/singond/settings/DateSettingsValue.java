package com.github.singond.settings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

public class DateSettingsValue extends MutableSettingsValue<Date> {

	private static final Function<Date, Date> DUPLICATOR
			= d -> new Date(d.getTime());
	private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final Function<Date, String> TO_STRING
			= d -> FORMAT.format(d);
	private static final Function<String, Date> FROM_STRING
			= s -> {
				try {
					return FORMAT.parse(s);
				} catch (ParseException e) {
					e.printStackTrace();
					return null;
				}
			};

	public DateSettingsValue(String key, Date value) {
		super(key, value, DUPLICATOR, TO_STRING, FROM_STRING);
	}

}
