package com.github.singond.melodear.desktop.settings;

import javafx.beans.property.Property;

/**
 * An implementation of {@link SettingsValue} suitable for wrapping
 * an immutable value.
 * An immutable value cannot be modified once created and thus can be freely
 * passed directly into copies of the tree.
 * <strong>It is solely the responsibility of the user of this class
 * to provide an immutable value.</strong>
 * <p>
 * Note that the immutability applies to the wrapped value, not to this
 * object as a whole. The value of this node can still be modified
 * by replacing it with another instance.
 *
 * @author Singon
 * @param <T> the type of value held by this node
 */
public class ImmutableSettingsValue<T>
		implements SettingsValue<T, ImmutableSettingsValue<T>> {

	private final String key;
	private final Property<T> value;

	public ImmutableSettingsValue(String key, Property<T> valueProperty) {
		this.key = key;
		this.value = valueProperty;
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
		return value.getValue();
	}

	@Override
	public void updateFrom(ImmutableSettingsValue<T> src) {
		value.setValue(src.valueCopy());
	}

}