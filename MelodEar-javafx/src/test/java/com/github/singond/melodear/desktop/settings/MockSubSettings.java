package com.github.singond.melodear.desktop.settings;

import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MockSubSettings extends AbstractSettingsTree<MockSubSettings> {

	/** An example of an immutable property. */
	private final IntegerProperty integer = new SimpleIntegerProperty();
	/** An example of a mutable property. */
	private final ObjectProperty<Date> date = new SimpleObjectProperty<>(new Date());

	public MockSubSettings() {
		super("MockSettings");
		addItem(new ImmutableSettingsValue<Number>("integer", integer));
		addItem(new MutableSettingsValue<Date>("date", date,
				d -> new Date(d.getTime())));
	}

	@Override
	protected MockSubSettings newInstance() {
		return new MockSubSettings();
	}

	public Integer getInteger() {
		return integer.get();
	}

	public void setInteger(Integer value) {
		integer.set(value);
	}

	public Date getDate() {
		return date.get();
	}

	public void setDate(Date date) {
		this.date.setValue(date);
	}
}
