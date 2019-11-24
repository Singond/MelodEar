package com.github.singond.settings;

import java.util.function.Function;

/**
 * An implementation of {@link SettingsValueNode} suitable for wrapping
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
		extends ConvertableSettingsValue<T, ImmutableSettingsValue<T>> {

	public ImmutableSettingsValue(String key, T value,
			Function<T, String> toString, Function<String, T> fromString) {
		super(key, value, toString, fromString);
	}

	public ImmutableSettingsValue(String key, T value,
			Function<String, T> fromString) {
		super(key, value, fromString);
	}

	@Override
	public final T valueCopy() {
		return super.valueCopy();
	}

}