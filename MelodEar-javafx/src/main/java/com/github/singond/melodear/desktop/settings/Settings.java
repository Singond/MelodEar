package com.github.singond.melodear.desktop.settings;

/**
 * An aggregation of user settings.
 *
 * @author Singon
 * @param <T> the concrete subtype of {@code Settings}
 */
public interface Settings<T extends Settings<?>> {

	/**
	 * Returns a new instance of this subtype of {@code Settings} which is
	 * identical to this one.
	 *
	 * @return a copy of this instance
	 */
	T copy();

	/**
	 * Updates this instance to contain exactly the same data as the given
	 * instance of the same class.
	 *
	 * @param src the original whose fields are to be coped to this instance
	 */
	void updateFrom(T src);
}
