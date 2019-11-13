package com.github.singond.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Skeletal implementation of the {@link SettingsTree} interface.
 * <p>
 * <strong>Important:</strong> Note that all types in the
 * {@code com.github.singond.settings} package rely heavily
 * on the fact that in each generic type, the type parameter {@code <S>}
 * is the type itself. Undefined behaviour occurs if this requirement
 * is violated.
 *
 * @author Singon
 * @param <S> the concrete subclass of {@code AbstractSettingsTree}
 */
public abstract class AbstractSettingsTree<S extends AbstractSettingsTree<S>>
		extends AbstractSettingsNode<S>
		implements SettingsTree<S> {

	private static Logger logger
			= LogManager.getLogger(AbstractSettingsTree.class);

	private final Map<String, SettingsNode<?>> nodes = new HashMap<>();

	public AbstractSettingsTree(String key) {
		super(key);
	}

	/**
	 * Adds a new node (either a value node or a tree node) to this tree.
	 * <p>
	 * The return value is the argument itself. It serves as a syntactic sugar
	 * to make initialization of a field in a constructor easier,
	 * as in the following example:
	 * <pre>
	 *   private final SettingsValue&lt;Integer> myVal;
	 *
	 *   public MySettings() {
	 *     myVal = newNode(new ImmutableSettingsValue&lt;Integer>("myVal", null));
	 *   }
	 * </pre>
	 *
	 * @param node the node to be added
	 * @return the {@code item} argument itself
	 */
	protected final <T extends SettingsNode<?>, U extends T> U newNode(U node) {
		if (node == null) {
			throw new NullPointerException("Cannot insert null item");
		} else if (node.key() == null) {
			throw new NullPointerException("Cannot insert item with null key");
		}
		node.setParent(this);
		nodes.put(node.key(), node);
		return node;
	}

	public final SettingsNode<?> getNode(String key) {
		return nodes.get(key);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final void updateWith(S src) {
		logger.debug("Updating from {}", src);
		for (Entry<String, SettingsNode<?>> e : nodes.entrySet()) {
			SettingsNode item = e.getValue();
			if (!e.getKey().equals(item.key())) {
				throw new AssertionError(
						"SettingsTree item found under different key from its own");
			}
			item.updateWith(src.getNode(item.key()));
		}
	}

	@Override
	public final void invite(SettingsNodeVisitor visitor) {
		for (SettingsNode<?> node : nodes.values()) {
			node.invite(visitor);
		}
		visitor.visitTree(this);
	}

	protected abstract S newInstance();

	@SuppressWarnings("unchecked")
	@Override
	public final S copy() {
		S copy = newInstance();
		copy.updateWith((S)this);
		return copy;
	}

}
