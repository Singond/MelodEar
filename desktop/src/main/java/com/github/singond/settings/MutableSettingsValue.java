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
		extends ConvertableSettingsValue<T, MutableSettingsValue<T>> {

	private final Function<T, T> duplicator;

	public MutableSettingsValue(String key, T value,
			Function<T, T> valueDuplicator,
			Function<T, String> toString, Function<String, T> fromString) {
		super(key, value, toString, fromString);
		if (valueDuplicator == null) {
			throw new NullPointerException(
					"Value duplicator must not be null");
		}
		this.duplicator = valueDuplicator;
	}

	public MutableSettingsValue(String key, T value,
			Function<T, T> valueDuplicator,
			Function<String, T> fromString) {
		this(key, value, valueDuplicator, t -> t.toString(), fromString);
	}

	@Override
	public T valueCopy() {
		T val = value();
		if (val != null) {
			return duplicator.apply(val);
		} else {
			return null;
		}
	}

}