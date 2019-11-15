package com.github.singond.settings;

/**
 * A branch node of the settings tree, which holds nested settings
 * and its own type.
 * This is an extension of {@link SettingsTree}, which includes its own
 * concrete subtype as the type parameter {@code <S>}.
 * <p>
 * <strong>Important:</strong> Note that all types in the
 * {@code com.github.singond.settings} package rely heavily
 * on the fact that in each generic type, the type parameter {@code <S>}
 * is the type itself. Undefined behaviour occurs if this requirement
 * is violated.
 *
 * @author Singon
 * @param <S> the concrete subtype of {@code SettingsTreeNode}
 */
public interface SettingsTreeNode<S extends SettingsTreeNode<S>>
		extends SettingsNode<S>, SettingsTree {

	/**
	 * Returns a new instance of this subtype of {@code SettingsTreeNode}
	 * which is identical to this one.
	 *
	 * @return a copy of this instance
	 */
	S copy();

}
