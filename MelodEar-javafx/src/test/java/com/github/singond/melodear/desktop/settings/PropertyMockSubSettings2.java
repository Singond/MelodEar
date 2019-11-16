package com.github.singond.melodear.desktop.settings;

import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public class PropertyMockSubSettings2
		extends PropertyAbstractSettingsTree<PropertyMockSubSettings2> {

	/** An example of an immutable property. */
	private final IntegerProperty integer;
	/** An example of a mutable property. */
	private final Property<Date> date;

	public PropertyMockSubSettings2() {
		super("MockSubSettings");
		integer = newPropertyNode("integer", Integer.valueOf(0));
		date = newPropertyNode("date", new SimpleObjectProperty<>(),
				d -> new Date(d.getTime()), new DateStringConverter());
	}

	@Override
	protected PropertyMockSubSettings2 newInstance(String key) {
		return new PropertyMockSubSettings2();
	}

	public Integer getInteger() {
		return integer.get();
	}

	public void setInteger(Integer value) {
		integer.set(value);
	}

	public Date getDate() {
		return date.getValue();
	}

	public void setDate(Date date) {
		this.date.setValue(date);
	}
}
