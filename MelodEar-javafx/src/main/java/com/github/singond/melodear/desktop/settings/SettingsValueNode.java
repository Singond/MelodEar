package com.github.singond.melodear.desktop.settings;

/**
 * A leaf node of the settings tree, which holds a value and its own type.
 * This is an extension of {@link SettingsValue}, which includes its own
 * concrete subtype as the type parameter {@code <S>}.
 * <p>
 * <strong>Important:</strong> Note that all types in the
 * {@code com.github.singond.melodear.desktop.settings} package rely heavily
 * on the fact that in each generic type, the type parameter {@code <S>}
 * is the type itself. Undefined behaviour occurs if this requirement
 * is violated.
 *
 * @author Singon
 * @param <T> the type of value held by this node
 * @param <S> the concrete subtype of {@code SettingsValueNode}
 */
public interface SettingsValueNode<T, S extends SettingsValueNode<T, S>>
		extends SettingsNode<S>, SettingsValue<T> {}