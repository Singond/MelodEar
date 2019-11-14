package com.github.singond.settings;

/**
 * A settings value node which holds a {@link Double} object.
 *
 * @author Singon
 */
public final class EnumSettingsValue<E extends Enum<E>>
		extends AbstractSettingsValue<E, EnumSettingsValue<E>> {

	private final E typeRef;

	public EnumSettingsValue(String key, E value) {
		super(key, value);
		// We need a non-null value as a type reference
		if (value == null) {
			throw new NullPointerException("Value must not be null");
		}
		this.typeRef = value;
	}

	@Override
	public final String valueToString() {
		return value().name();
	}

	@SuppressWarnings("unchecked")
	@Override
	public final E valueFromString(String string) {
		return (E) Enum.valueOf(typeRef.getClass(), string);
	}

}
