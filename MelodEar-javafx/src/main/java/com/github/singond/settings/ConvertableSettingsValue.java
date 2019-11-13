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
abstract class ConvertableSettingsValue
		<T, S extends ConvertableSettingsValue<T, S>>
		extends AbstractSettingsValue<T, S> {

	private final Function<T, String> toStringConverter;
	private final Function<String, T> fromStringConverter;

	public ConvertableSettingsValue(String key, T value,
			Function<T, String> toString, Function<String, T> fromString) {
		super(key, value);
		if (toString == null) {
			throw new NullPointerException(
					"Converter to string must not be null");
		}
		if (fromString == null) {
			throw new NullPointerException(
					"Converter from string must not be null");
		}
		this.toStringConverter = toString;
		this.fromStringConverter = fromString;
	}

	public ConvertableSettingsValue(String key, T value,
			Function<String, T> fromString) {
		this(key, value, t -> t.toString(), fromString);
	}

	@Override
	public final String valueToString() {
		return toStringConverter.apply(value());
	}

	@Override
	public final T valueFromString(String string) {
		return fromStringConverter.apply(string);
	}

}