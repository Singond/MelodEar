package com.github.singond.settings;

/**
 * A leaf node of the settings tree, which holds a value.
 *
 * @author Singon
 * @param <T> the type of value held by this node
 */
public interface SettingsValue<T> {

	/**
	 * Returns the value of this node.
	 *
	 * @return the value of this node
	 */
	T value();

	/**
	 * Sets the value of this node.
	 *
	 * @param value the value to be set to this node
	 */
	void setValue(T value);

	/**
	 * Returns an object equal to the value of this node
	 * (by means of {@link Object#equals}), which does not expose the value
	 * of this node to changes.
	 *
	 * This means that any changes to the returned object
	 * <strong>must not</strong> propagate to the value of this node.
	 * If the value of this node is an immutable type, this method may return
	 * the value directly.
	 *
	 * @return an object equal to the value of this node
	 */
	T valueCopy();

	/**
	 * Converts the value of this node to a format, from which it can be
	 * retreived by {@link #valueFromString}.
	 *
	 * @return a string representation of {@link #value}
	 */
	String valueToString();

	/**
	 * Converts the string created by {@link #valueToString} in an object of
	 * the same type as this back into a value of type {@code T}.
	 *
	 * @param string the string to be converted
	 * @return value read from {@code string}
	 */
	T valueFromString(String string);

	/**
	 * Sets the value of this node to the value represented by the given
	 * string. This is equivalent to calling
	 * {@code setValue(this.valueFromString(string))}.
	 *
	 * @param string
	 * @see {@link #valueFromString}
	 */
	void setValueFromString(String string);
}
