package com.github.singond.settings;

/**
 * A visitor to settings tree nodes.
 *
 * @author Singon
 */
public interface SettingsNodeVisitor {

	void visitValue(SettingsValue<?> value);

	void visitTree(SettingsTree<?> tree);

}
