package com.github.singond.melodear.desktop.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import javafx.beans.property.Property;

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
 * @param <S> the concrete subclass of {@code AbstractSettings}
 */
public abstract class AbstractSettings<S extends AbstractSettings<S>>
		implements SettingsTree<S> {

	private static Logger logger = LogManager.getLogger(AbstractSettings.class);

	private final String key;
	protected final Map<String, SettingsNode<?>> items = new HashMap<>();

	public AbstractSettings(String key) {
		this.key = key;
	}

	protected void addItem(SettingsNode<?> item) {
		if (item == null) {
			throw new NullPointerException("Cannot insert null item");
		} else if (item.key() == null) {
			throw new NullPointerException("Cannot insert item with null key");
		}
		items.put(item.key(), item);
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

	public static class BasicSettingsItem<T> implements SettingsValue<T, BasicSettingsItem<T>> {

		private final String key;
		private final Property<T> value;
		private Function<T, T> duplicator;

		public BasicSettingsItem(String key, Property<T> valueProperty) {
			this.key = key;
			this.value = valueProperty;
			this.duplicator = v -> v;
		}

		public BasicSettingsItem(String key, Property<T> valueProperty,
				Function<T, T> valueDuplicator) {
			this.key = key;
			this.value = valueProperty;
			this.duplicator = valueDuplicator;
		}

		@Override
		public String key() {
			return key;
		}

		@Override
		public T value() {
			return value.getValue();
		}

		@Override
		public T valueCopy() {
			return duplicator.apply(value.getValue());
		}

		@Override
		public void updateFrom(BasicSettingsItem<T> src) {
			value.setValue(src.valueCopy());
		}
	}
}
