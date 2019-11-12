package com.github.singond.settings;

import java.util.Date;

import com.github.singond.settings.AbstractSettingsTree;
import com.github.singond.settings.ImmutableSettingsValue;
import com.github.singond.settings.MutableSettingsValue;
import com.github.singond.settings.SettingsValue;

public class MockSubSettings extends AbstractSettingsTree<MockSubSettings> {

	/** An example of an immutable property. */
	private final SettingsValue<Integer> integer;
	/** An example of a mutable property. */
	private final SettingsValue<Date> date;

	public MockSubSettings() {
		super("MockSubSettings");
		integer = newNode(new ImmutableSettingsValue<Integer>("integer", null));
		date = newNode(new MutableSettingsValue<Date>(
				"date", null, d -> new Date(d.getTime())));
	}

	@Override
	protected MockSubSettings newInstance() {
		return new MockSubSettings();
	}

	public Integer getInteger() {
		return integer.value();
	}

	public void setInteger(Integer value) {
		integer.setValue(value);
	}

	public Date getDate() {
		return date.value();
	}

	public void setDate(Date date) {
		this.date.setValue(date);
	}

}
