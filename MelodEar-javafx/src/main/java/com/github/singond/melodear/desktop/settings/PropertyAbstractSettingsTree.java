package com.github.singond.melodear.desktop.settings;

import java.util.function.Function;

import javafx.beans.property.Property;

public abstract class PropertyAbstractSettingsTree<S extends PropertyAbstractSettingsTree<S>>
		extends AbstractSettingsTree<S> {

	public PropertyAbstractSettingsTree(String key) {
		super(key);
	}

	protected <T, P extends Property<T>> P newPropertyNode(String key,
			P property, Function<T, T> propertyValueDuplicator) {
		super.newNode(new PropertySettingsValue<T>(key, property, propertyValueDuplicator));
		return property;
	}

	protected <T, P extends Property<T>> P newPropertyNode(String key,
			P property) {
		return newPropertyNode(key, property, Function.identity());
	}

	private static final class PropertySettingsValue<T>
			extends AbstractSettingsValue<T, PropertySettingsValue<T>> {

		private final Property<T> property;
		private final Function<T, T> duplicator;

		public PropertySettingsValue(String key, Property<T> property,
				Function<T, T> duplicator) {
			super(key);
			this.property = property;
			this.duplicator = duplicator;
		}

		@Override
		public T value() {
			return property.getValue();
		}

		@Override
		public void setValue(T value) {
			property.setValue(value);
		}

		@Override
		public T valueCopy() {
			T value = property.getValue();
			if (value != null) {
				return duplicator.apply(property.getValue());
			} else {
				return null;
			}
		}

		@Override
		public void updateWith(PropertySettingsValue<T> src) {
			property.setValue(src.valueCopy());
		}
	}
}
