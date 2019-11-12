package com.github.singond.settings;

import java.util.function.Function;

/**
 * An implementation of {@link SettingsValueNode} which wraps a mutable value.
 * A mutable value requires creating a defensive copy in the copies of the
 * tree to prevent modifications from propagating into the original.
 *
 * @author Singon
 * @param <T> the type of value held by this node
 */
public class MutableSettingsValue<T>
		extends AbstractSettingsValue<T, MutableSettingsValue<T>> {

	private T value;
	private final Function<T, T> duplicator;

	public MutableSettingsValue(String key, T value,
			Function<T, T> valueDuplicator) {
		super(key);
		this.value = value;
		this.duplicator = valueDuplicator;
	}

	@Override
	public T value() {
		return value;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public T valueCopy() {
		if (value != null) {
			return duplicator.apply(value);
		} else {
			return null;
		}
	}

	@Override
	public void updateWith(MutableSettingsValue<T> src) {
		if (src != null) {
			value = src.valueCopy();
		} else {
			value = null;
		}
	}

}