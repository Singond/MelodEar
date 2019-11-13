package com.github.singond.melodear.desktop.settings;

import java.util.function.Function;

import javafx.beans.property.Property;
import javafx.util.StringConverter;

import com.github.singond.settings.AbstractSettingsNode;
import com.github.singond.settings.AbstractSettingsTree;
import com.github.singond.settings.SettingsNodeVisitor;
import com.github.singond.settings.SettingsValueNode;

public abstract class PropertyAbstractSettingsTree
		<S extends PropertyAbstractSettingsTree<S>>
		extends AbstractSettingsTree<S> {

	public PropertyAbstractSettingsTree(String key) {
		super(key);
	}

	protected <T, P extends Property<T>> P newPropertyNode(String key,
			P property, Function<T, T> propertyValueDuplicator,
			StringConverter<T> stringConverter) {
		super.newNode(new PropertySettingsValue<T>(key, property,
				propertyValueDuplicator, stringConverter));
		return property;
	}

	protected <T, P extends Property<T>> P newPropertyNode(String key,
			P property, StringConverter<T> stringConverter) {
		return newPropertyNode(key, property, Function.identity(),
				stringConverter);
	}

	private static final class PropertySettingsValue<T>
			extends AbstractSettingsNode<PropertySettingsValue<T>>
			implements SettingsValueNode<T, PropertySettingsValue<T>> {

		private final Property<T> property;
		private final Function<T, T> duplicator;
		private final StringConverter<T> stringConverter;

		public PropertySettingsValue(String key, Property<T> property,
				Function<T, T> duplicator,
				StringConverter<T> stringConverter) {
			super(key);
			this.property = property;
			this.duplicator = duplicator;
			this.stringConverter = stringConverter;
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
		public String valueToString() {
			return stringConverter.toString(value());
		}

		@Override
		public T valueFromString(String string) {
			return stringConverter.fromString(string);
		}

		@Override
		public void setValueFromString(String string) {
			setValue(valueFromString(string));
		}

		@Override
		public void updateWith(PropertySettingsValue<T> src) {
			property.setValue(src.valueCopy());
		}

		@Override
		public void invite(SettingsNodeVisitor visitor) {
			visitor.visitValue(this);
		}
	}
}
