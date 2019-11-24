package com.github.singond.settings;

/**
 * A settings value node which holds a {@link Double} object.
 *
 * @author Singon
 */
public final class DoubleSettingsValue
		extends AbstractSettingsValue<Double, DoubleSettingsValue> {

	public DoubleSettingsValue(String key, Double value) {
		super(key, value);
	}

	@Override
	public final String valueToString() {
		return value().toString();
	}

	@Override
	public final Double valueFromString(String string) {
		return Double.valueOf(string);
	}

}
