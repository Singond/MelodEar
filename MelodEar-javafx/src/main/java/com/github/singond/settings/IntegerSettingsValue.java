package com.github.singond.settings;

/**
 * A settings value node which holds a {@link Integer} object.
 *
 * @author Singon
 */
public final class IntegerSettingsValue
		extends AbstractSettingsValue<Integer, IntegerSettingsValue> {

	public IntegerSettingsValue(String key, Integer value) {
		super(key, value);
	}

	@Override
	public final String valueToString() {
		return value().toString();
	}

	@Override
	public final Integer valueFromString(String string) {
		return Integer.valueOf(string);
	}

}
