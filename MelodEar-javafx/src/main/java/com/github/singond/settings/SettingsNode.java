package com.github.singond.settings;

/**
 * An element (a branch node or a leaf node) of a settings tree.
 * <p>
 * <strong>Important:</strong> Note that all types in the
 * {@code com.github.singond.settings} package rely heavily
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

	/**
	 * Invites a visitor to this node and its children.
	 * In this method, each subtype of this type must call the appropriate
	 * method in the visitor, with itself as the argument.
	 * Value nodes must call the {@link SettingsNodeVisitor#visitValue} method,
	 * while tree nodes must call {@link SettingsNodeVisitor#visitTree}.
	 *
	 * @param visitor the visitor to be invited
	 */
	void invite(SettingsNodeVisitor visitor);
}