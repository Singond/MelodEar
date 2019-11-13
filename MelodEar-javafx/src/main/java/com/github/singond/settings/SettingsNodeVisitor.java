package com.github.singond.settings;

/**
 * A visitor to settings tree nodes.
 *
 * @author Singon
 * @param <A> the argument type of the {@code visit*} method
 * @param <R> the return type of the {@code visit*} method
 */
public interface SettingsNodeVisitor<A, R> {

	R visitValue(SettingsValue<?> value, A arg);

	R visitTree(SettingsTree<?> tree, A arg);

}
