package com.github.singond.settings;

/**
 * A settings value node which holds a {@link String} object.
 *
 * @author Singon
 */
public final class StringSettingsValue
		extends AbstractSettingsValue<String, StringSettingsValue> {

	public StringSettingsValue(String key, String value) {
		super(key, value);
	}

	@Override
	public final String valueToString() {
		return value();
	}

	@Override
	public final String valueFromString(String string) {
		return string;
	}

}
