package com.github.singond.settings;

import java.util.LinkedList;
import java.util.List;

/**
 * Skeletal implementation of {@link SettingsNode}.
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
	private SettingsNode<?> parent = null;

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

	@Override
	public final SettingsNode<?> parent() {
		return parent;
	}

	@Override
	public final void setParent(SettingsNode<?> parent) {
		if (this.parent != null) {
			throw new IllegalStateException("Cannot set parent more than once. "
					+ "Node " + this + " already has a parent: " + parent());
		}
		this.parent = parent;
	}

	@Override
	public List<SettingsNode<?>> ancestors() {
		if (parent == null) {
			return new LinkedList<>();
		} else {
			List<SettingsNode<?>> parentAncestors = parent().ancestors();
			parentAncestors.add(parent);
			return parentAncestors;
		}
	}

}