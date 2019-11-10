package com.github.singond.melodear.desktop.settings;

/**
 * A branch node of the settings tree, which holds nested settings.
 * <p>
 * <strong>Important:</strong> Note that all types in the
 * {@code com.github.singond.melodear.desktop.settings} package rely heavily
 * on the fact that in each generic type, the type parameter {@code <S>}
 * is the type itself. Undefined behaviour occurs if this requirement
 * is violated.
 *
 * @author Singon
 * @param <S> the concrete subtype of {@code SettingsTree}
 */
public interface SettingsTree<S extends SettingsTree<S>>
		extends SettingsNode<S> {

	/**
	 * Returns a new instance of this subtype of {@code SettingsTree} which is
	 * identical to this one.
	 *
	 * @return a copy of this instance
	 */
	S copy();

}
