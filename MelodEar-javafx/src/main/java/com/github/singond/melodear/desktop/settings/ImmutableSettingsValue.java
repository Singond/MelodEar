package com.github.singond.melodear.desktop.settings;

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
		extends AbstractSettingsValue<T, ImmutableSettingsValue<T>> {

	private T value;

	public ImmutableSettingsValue(String key, T value) {
		super(key);
		this.value = value;
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
		return value;
	}

	@Override
	public void updateFrom(ImmutableSettingsValue<T> src) {
		if (src != null) {
			value = src.valueCopy();
		} else {
			value = null;
		}
	}

}