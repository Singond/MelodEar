package com.github.singond.settings;

import java.util.List;

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
	 * Returns the parent node of this node, if any.
	 * If no parent has been assigned yet, this method returns {@code null}.
	 *
	 * @return parent node or {@code null}
	 */
	SettingsNode<?> parent();

	/**
	 * Sets the parent of this node.
	 * This method must be called when adding this node as a child to another
	 * node.
	 * This method may only be called once. Subsequent calls will fail with
	 * {@link IllegalStateException}.
	 *
	 * @param parent the parent node
	 * @throws IllegalStateException if this method has been called when
	 *         this node already has a parent
	 */
	void setParent(SettingsNode<?> parent);

	/**
	 * Returns all ancestors of this node, starting with the root node
	 * and proceeding downwards to the parent of this node.
	 *
	 * @return ancestors of this node
	 */
	List<SettingsNode<?>> ancestors();

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