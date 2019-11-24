package com.github.singond.settings;

/**
 * A visitor to settings tree nodes.
 *
 * @author Singon
 */
public interface SettingsNodeVisitor {

	void visitValue(SettingsValueNode<?, ?> value);

	void visitTree(SettingsTreeNode<?> tree);

}
