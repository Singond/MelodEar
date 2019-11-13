package com.github.singond.settings;

/**
 * Skeletal implementation of {@link SettingsValueNode}.
 * <p>
 * <strong>Important:</strong> Note that all types in the
 * {@code com.github.singond.settings} package rely heavily
 * on the fact that in each generic type, the type parameter {@code <S>}
 * is the type itself. Undefined behaviour occurs if this requirement
 * is violated.
 *
 * @author Singon
 * @param <S> the concrete subtype of {@code SettingsValueNode}
 */
public abstract class AbstractSettingsNode<S extends AbstractSettingsNode<S>>
		implements SettingsNode<S> {

	private final String key;

	/**
	 * Creates a new instance of settings node with the given key.
	 * This constructor is intended only for subclasses of this class.
	 *
	 * @param key the key of the value. Must not be {@code null}.
	 * @throws NullPointerException if {@code key} is null
	 */
	protected AbstractSettingsNode(String key) {
		if (key == null) {
			throw new NullPointerException("Key must not be null");
		}
		this.key = key;
	}

	@Override
	public final String key() {
		return key;
	}

}