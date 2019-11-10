package com.github.singond.melodear.desktop.settings;

import java.util.Date;

public class MockSubSettings extends AbstractSettingsTree<MockSubSettings> {

	/** An example of an immutable property. */
	private final SettingsValue<Integer,?> integer
			= new ImmutableSettingsValue<Integer>("integer", null);
	/** An example of a mutable property. */
	private final SettingsValue<Date,?> date = new MutableSettingsValue<Date>(
			"date", null, d -> new Date(d.getTime()));

	public MockSubSettings() {
		super("MockSettings");
		addItem(integer);
		addItem(date);
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
