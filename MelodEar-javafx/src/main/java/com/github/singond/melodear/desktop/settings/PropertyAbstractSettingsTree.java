package com.github.singond.melodear.desktop.settings;

import java.util.function.Function;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
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
		super.newNode(new ConvertablePropertySettingsValue<T>(key, property,
				propertyValueDuplicator, stringConverter));
		return property;
	}

	protected <T, P extends Property<T>> P newPropertyNode(String key,
			P property, StringConverter<T> stringConverter) {
		return newPropertyNode(key, property, Function.identity(),
				stringConverter);
	}

	protected <T, P extends Property<T>> P newPropertyNode(String key,
			P property, Function<T, T> propertyValueDuplicator) {
		super.newNode(new NonconvertablePropertySettingsValue<T>(
				key, property, propertyValueDuplicator));
		return property;
	}

	protected <T, P extends Property<T>> P newPropertyNode(String key,
			P property) {
		super.newNode(new NonconvertablePropertySettingsValue<T>(
				key, property, Function.identity()));
		return property;
	}

	protected <E extends Enum<E>> Property<E> newPropertyNode(
			String key, E value) {
		return newPropertyNode(key, new SimpleObjectProperty<>(value),
				Function.identity(), new EnumStringConverter<>(value));
	}

	private abstract static class PropertySettingsValue
			<T, S extends PropertySettingsValue<T, S>>
			extends AbstractSettingsNode<S>
			implements SettingsValueNode<T, S> {

		private final Property<T> property;
		private final Function<T, T> duplicator;

		public PropertySettingsValue(String key, Property<T> property,
				Function<T, T> duplicator) {
			super(key);
			this.property = property;
			this.duplicator = duplicator;
		}

		@Override
		public final T value() {
			return property.getValue();
		}

		@Override
		public final void setValue(T value) {
			property.setValue(value);
		}

		@Override
		public final T valueCopy() {
			T value = property.getValue();
			if (value != null) {
				return duplicator.apply(property.getValue());
			} else {
				return null;
			}
		}

		@Override
		public final void setValueFromString(String string) {
			setValue(valueFromString(string));
		}

		@Override
		public final void updateWith(S src) {
			property.setValue(src.valueCopy());
		}

		@Override
		public final void invite(SettingsNodeVisitor visitor) {
			visitor.visitValue(this);
		}
	}

	private static final class NonconvertablePropertySettingsValue<T>
			extends PropertySettingsValue<T, NonconvertablePropertySettingsValue<T>> {

		public NonconvertablePropertySettingsValue(String key,
				Property<T> property, Function<T, T> duplicator) {
			super(key, property, duplicator);
		}

		@Override
		public String valueToString() {
			return "";
		}

		@Override
		public T valueFromString(String string) {
			return null;
		}
	}

	private static final class ConvertablePropertySettingsValue<T>
			extends PropertySettingsValue<T, ConvertablePropertySettingsValue<T>> {

		private final StringConverter<T> stringConverter;

		public ConvertablePropertySettingsValue(String key,
				Property<T> property, Function<T, T> duplicator,
				StringConverter<T> stringConverter) {
			super(key, property, duplicator);
			this.stringConverter = stringConverter;
		}

		@Override
		public String valueToString() {
			return stringConverter.toString(value());
		}

		@Override
		public T valueFromString(String string) {
			return stringConverter.fromString(string);
		}
	}
}
