package com.github.singond.melodear.desktop.settings;

/**
 * A leaf node of the settings tree, which holds a value.
 * <p>
 * <strong>Important:</strong> Note that all types in the
 * {@code com.github.singond.melodear.desktop.settings} package rely heavily
 * on the fact that in each generic type, the type parameter {@code <S>}
 * is the type itself. Undefined behaviour occurs if this requirement
 * is violated.
 *
 * @author Singon
 * @param <T> the type of value held by this node
 * @param <S> the concrete subtype of {@code SettingsValue}
 */
public interface SettingsValue<T, S extends SettingsValue<T, S>>
		extends SettingsNode<S> {

	/**
	 * Returns the value of this node.
	 *
	 * @return the value of this node
	 */
	T value();

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

}