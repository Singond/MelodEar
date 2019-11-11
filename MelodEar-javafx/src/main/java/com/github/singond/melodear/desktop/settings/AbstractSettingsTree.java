package com.github.singond.melodear.desktop.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Skeletal implementation of the {@link SettingsTree} interface.
 * <p>
 * <strong>Important:</strong> Note that all types in the
 * {@code com.github.singond.melodear.desktop.settings} package rely heavily
 * on the fact that in each generic type, the type parameter {@code <S>}
 * is the type itself. Undefined behaviour occurs if this requirement
 * is violated.
 *
 * @author Singon
 * @param <S> the concrete subclass of {@code AbstractSettingsTree}
 */
public abstract class AbstractSettingsTree<S extends AbstractSettingsTree<S>>
		implements SettingsTree<S> {

	private static Logger logger
			= LogManager.getLogger(AbstractSettingsTree.class);

	private final String key;
	protected final Map<String, SettingsNode<?>> items = new HashMap<>();

	public AbstractSettingsTree(String key) {
		this.key = key;
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
	 * @param item the node to be added
	 * @return the {@code item} argument itself
	 */
	protected <T extends SettingsNode<?>> T newNode(T item) {
		if (item == null) {
			throw new NullPointerException("Cannot insert null item");
		} else if (item.key() == null) {
			throw new NullPointerException("Cannot insert item with null key");
		}
		items.put(item.key(), item);
		return item;
	}

	public SettingsNode<?> getItem(String key) {
		return items.get(key);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final void updateFrom(S src) {
		logger.debug("Updating from {}", src);
		for (Entry<String, SettingsNode<?>> e : items.entrySet()) {
			SettingsNode item = e.getValue();
			if (!e.getKey().equals(item.key())) {
				throw new AssertionError(
						"SettingsTree item found under different key from its own");
			}
			item.updateFrom(src.getItem(item.key()));
		}
	}

	protected abstract S newInstance();

	@SuppressWarnings("unchecked")
	@Override
	public S copy() {
		S copy = newInstance();
		copy.updateFrom((S)this);
		return copy;
	}

	@Override
	public String key() {
		return key;
	}

}
