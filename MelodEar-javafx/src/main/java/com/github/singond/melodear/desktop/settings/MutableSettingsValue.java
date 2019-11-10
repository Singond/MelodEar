package com.github.singond.melodear.desktop.settings;

import java.util.function.Function;

import javafx.beans.property.Property;

/**
 * An implementation of {@link SettingsValue} which wraps a mutable value.
 * A mutable value requires creating a defensive copy in the copies of the
 * tree to prevent modifications from propagating into the original.
 *
 * @author Singon
 * @param <T> the type of value held by this node
 */
public class MutableSettingsValue<T>
		implements SettingsValue<T, MutableSettingsValue<T>> {

	private final String key;
	private final Property<T> value;
	private Function<T, T> duplicator;

	public MutableSettingsValue(String key, Property<T> valueProperty,
			Function<T, T> valueDuplicator) {
		this.key = key;
		this.value = valueProperty;
		this.duplicator = valueDuplicator;
	}

	@Override
	public String key() {
		return key;
	}

	@Override
	public T value() {
		return value.getValue();
	}

	@Override
	public T valueCopy() {
		return duplicator.apply(value.getValue());
	}

	@Override
	public void updateFrom(MutableSettingsValue<T> src) {
		value.setValue(src.valueCopy());
	}

}