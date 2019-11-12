package com.github.singond.melodear.desktop.settings;

/**
 * An element (a branch node or a leaf node) of a settings tree.
 * <p>
 * <strong>Important:</strong> Note that all types in the
 * {@code com.github.singond.melodear.desktop.settings} package rely heavily
 * on the fact that in each generic type, the type parameter {@code <S>}
 * is the type itself. Undefined behaviour occurs if this requirement
 * is violated.
 *
 * @author Singon
 * @param <S> the concrete subtype of {@code SettingsNode}
 */
public interface SettingsNode<S extends SettingsNode<S>> {

	/**
	 * Returns a key unique among the siblings of this node in a tree.
	 * This key is used to distinguish nodes in the tree when copying.
	 *
	 * @return a unique key of this node
	 */
	String key();

	/**
	 * Sets the values in this node and its children to their corresponding
	 * values in the given source object.
	 *
	 * @param src the source object
	 */
	void updateWith(S src);

}