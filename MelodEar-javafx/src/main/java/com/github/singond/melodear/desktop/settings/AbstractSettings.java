package com.github.singond.melodear.desktop.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import javafx.beans.property.Property;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractSettings<S extends AbstractSettings<S>>
		implements Settings<S> {

	private static Logger logger = LogManager.getLogger(AbstractSettings.class);

	protected final Map<String, SettingsItem<?>> items = new HashMap<>();

	public AbstractSettings() {}

	public void addItem(SettingsItem<?> item) {
		if (item == null) {
			throw new NullPointerException("Cannot insert null item");
		} else if (item.key() == null) {
			throw new NullPointerException("Cannot insert item with null key");
		}
		items.put(item.key(), item);
	}

	public SettingsItem<?> getItem(String key) {
		return items.get(key);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final void updateFrom(S src) {
		logger.debug("Updating from {}", src);
		for (Entry<String, SettingsItem<?>> e : items.entrySet()) {
			SettingsItem item = e.getValue();
			if (!e.getKey().equals(item.key())) {
				throw new AssertionError(
						"Settings item found under different key from its own");
			}
			item.property().setValue(src.getItem(item.key()).valueCopy());
		}
	}

	public interface SettingsItem<T> {

		String key();

		T value();

		T valueCopy();

		Property<T> property();

	}

	public static class BasicSettingsItem<T> implements SettingsItem<T> {

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
		public Property<T> property() {
			return value;
		}

	}
}
