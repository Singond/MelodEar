package com.github.singond.settings;

import java.util.Date;

public class MockSubSettings extends AbstractSettingsTree<MockSubSettings> {

	/** An example of an immutable property. */
	private final SettingsValue<Integer> integer;
	/** An example of a mutable property. */
	private final SettingsValue<Date> date;

	public MockSubSettings(String key) {
		super(key);
		integer = newNode(new IntegerSettingsValue("integer", null));
		date = newNode(new DateSettingsValue("date", null));
	}

	public MockSubSettings() {
		this("MockSubSettings");
	}

	@Override
	protected MockSubSettings newInstance(String key) {
		return new MockSubSettings(key);
	}

	public Integer getInteger() {
		return integer.value();
	}

	public void setInteger(Integer value) {
		integer.setValue(value);
	}

	SettingsNode<?> integerNode() {
		return (SettingsNode<?>) integer;
	}

	public Date getDate() {
		return date.value();
	}

	public void setDate(Date date) {
		this.date.setValue(date);
	}

	SettingsNode<?> dateNode() {
		return (SettingsNode<?>) date;
	}

}
