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
 * @param <T> the type of value held by this node
 * @param <S> the concrete subtype of {@code SettingsValueNode}
 */
public abstract class AbstractSettingsValue<T, S extends AbstractSettingsValue<T, S>>
		extends AbstractSettingsNode<S>
		implements SettingsValueNode<T, S> {

	/**
	 * Creates a new instance of settings value with the given key.
	 * This constructor is intended only for subclasses of this class.
	 *
	 * @param key the key of the value. Must not be {@code null}.
	 * @throws NullPointerException if {@code key} is null
	 */
	protected AbstractSettingsValue(String key) {
		super(key);
	}

	@Override
	public final void invite(SettingsNodeVisitor visitor) {
		visitor.visitValue(this);
	}

}