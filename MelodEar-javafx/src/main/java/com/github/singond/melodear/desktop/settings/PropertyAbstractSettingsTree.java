package com.github.singond.melodear.desktop.settings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

	/**
	 * Adds a new {@code PropertySettingsValue} to this tree
	 * and returns its wrapped property.
	 *
	 * @param propval
	 * @return
	 */
	private <T, P extends Property<T>> P addPropertyNode(
			PropertySettingsValue<T, P, ?> propval) {
		super.newNode(propval);
		return propval.property();
	}

	protected <T, P extends Property<T>> P newPropertyNode(String key,
			P property, Function<T, T> propertyValueDuplicator,
			StringConverter<T> stringConverter) {
		return addPropertyNode(new ConvertablePropertySettingsValue<T, P>(
				key, property, propertyValueDuplicator, stringConverter));
	}

	protected <T, P extends Property<T>> P newPropertyNode(String key,
			P property, StringConverter<T> stringConverter) {
		return newPropertyNode(key, property, Function.identity(),
				stringConverter);
	}

	protected <T, P extends Property<T>> P newPropertyNode(String key,
			P property, Function<T, T> propertyValueDuplicator) {
		return addPropertyNode(new NonconvertablePropertySettingsValue<T, P>(
				key, property, propertyValueDuplicator));
	}

	protected <T, P extends Property<T>> P newPropertyNode(String key,
			P property) {
		return newPropertyNode(key, property, Function.identity());
	}

	protected IntegerProperty newPropertyNode(String key, Integer value) {
		return addPropertyNode(new IntegerPropertySettingsValue(key, value));
	}

	protected DoubleProperty newPropertyNode(String key, Double value) {
		return addPropertyNode(new DoublePropertySettingsValue(key, value));
	}

	protected StringProperty newPropertyNode(String key, String value) {
		return addPropertyNode(new StringPropertySettingsValue(key, value));
	}

	protected <E extends Enum<E>> Property<E> newPropertyNode(
			String key, E value) {
		return newPropertyNode(key, new SimpleObjectProperty<>(value),
				Function.identity(), new EnumStringConverter<>(value));
	}

	protected Property<Path> newPropertyNode(String key, Path value) {
		return addPropertyNode(new PathPropertySettingsValue(key, value));
	}

	private abstract static class PropertySettingsValue
			<T, P extends Property<T>, S extends PropertySettingsValue<T, P, S>>
			extends AbstractSettingsNode<S>
			implements SettingsValueNode<T, S> {

		private final P property;

		public PropertySettingsValue(String key, P property) {
			super(key);
			this.property = property;
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

		P property() {
			return property;
		}
	}

	private abstract static class CopiablePropertySettingsValue
			<T, P extends Property<T>, S extends CopiablePropertySettingsValue<T, P, S>>
			extends PropertySettingsValue<T, P, S> {

		private final Function<T, T> duplicator;

		public CopiablePropertySettingsValue(String key, P property,
				Function<T, T> duplicator) {
			super(key, property);
			this.duplicator = duplicator;
		}

		@Override
		public T valueCopy() {
			T value = value();
			if (value != null) {
				return duplicator.apply(value);
			} else {
				return null;
			}
		}
	}

	private static final class NonconvertablePropertySettingsValue
			<T, P extends Property<T>>
			extends CopiablePropertySettingsValue<T, P, NonconvertablePropertySettingsValue<T, P>> {

		public NonconvertablePropertySettingsValue(String key,
				P property, Function<T, T> duplicator) {
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

	private static final class ConvertablePropertySettingsValue
			<T, P extends Property<T>>
			extends CopiablePropertySettingsValue<T, P, ConvertablePropertySettingsValue<T, P>> {

		private final StringConverter<T> stringConverter;

		public ConvertablePropertySettingsValue(String key,
				P property, Function<T, T> duplicator,
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

	private static final class IntegerPropertySettingsValue
			extends PropertySettingsValue<Number, IntegerProperty, IntegerPropertySettingsValue> {

		public IntegerPropertySettingsValue(String key, Integer value) {
			super(key, new SimpleIntegerProperty(value));
		}

		@Override
		public Integer valueCopy() {
			return (Integer) value();
		}

		@Override
		public String valueToString() {
			return value().toString();
		}

		@Override
		public Integer valueFromString(String string) {
			return Integer.valueOf(string);
		}
	}

	private static final class DoublePropertySettingsValue
			extends PropertySettingsValue<Number, DoubleProperty, DoublePropertySettingsValue> {

		public DoublePropertySettingsValue(String key, Double value) {
			super(key, new SimpleDoubleProperty(value));
		}

		@Override
		public Double valueCopy() {
			return (Double) value();
		}

		@Override
		public String valueToString() {
			return value().toString();
		}

		@Override
		public Double valueFromString(String string) {
			return Double.valueOf(string);
		}
	}

	private static final class StringPropertySettingsValue
			extends PropertySettingsValue<String, StringProperty, StringPropertySettingsValue> {

		public StringPropertySettingsValue(String key, String value) {
			super(key, new SimpleStringProperty(value));
		}

		@Override
		public String valueCopy() {
			return value();
		}

		@Override
		public String valueToString() {
			return value();
		}

		@Override
		public String valueFromString(String string) {
			return string;
		}
	}

	private static final class PathPropertySettingsValue
			extends PropertySettingsValue<Path, Property<Path>, PathPropertySettingsValue> {

		public PathPropertySettingsValue(String key, Path value) {
			super(key, new SimpleObjectProperty<>(value));
		}

		@Override
		public Path valueCopy() {
			return value();
		}

		@Override
		public String valueToString() {
			return value().toString();
		}

		@Override
		public Path valueFromString(String string) {
			return Paths.get(string);
		}
	}
}
