package com.github.singond.melodear.desktop.settings;

import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import com.github.singond.settings.AbstractSettingsTree;
import com.github.singond.settings.MutableSettingsValue;
import com.github.singond.settings.SettingsValue;

public class PropertyMockSubSettings
		extends AbstractSettingsTree<PropertyMockSubSettings> {

	/** An example of an immutable property. */
	private final SettingsValue<IntegerProperty> integer;
	/** An example of a mutable property. */
	private final SettingsValue<Property<Date>> date;

	public PropertyMockSubSettings() {
		super("MockSubSettings");
		// Property is mutable, we need to use MutableSettingsValue everywhere
		integer = newNode(new MutableSettingsValue<IntegerProperty>(
				"integer", new SimpleIntegerProperty(),
				p -> new SimpleIntegerProperty(p.getValue()),
				p -> p.getValue().toString(),
				s -> new SimpleIntegerProperty(Integer.valueOf(s))));
		date = newNode(new DatePropertySettingsValue(
				"date", new SimpleObjectProperty<>(new Date())));
	}

	@Override
	protected PropertyMockSubSettings newInstance(String key) {
		return new PropertyMockSubSettings();
	}

	public Integer getInteger() {
		return integer.value().get();
	}

	public void setInteger(Integer value) {
		integer.value().set(value);
	}

	public Date getDate() {
		return date.value().getValue();
	}

	public void setDate(Date date) {
		this.date.value().setValue(date);
	}
}
