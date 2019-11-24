package com.github.singond.melodear.desktop.settings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.github.singond.settings.AbstractSettingsTree;
import com.github.singond.settings.MutableSettingsValue;
import com.github.singond.settings.SettingsValue;

public class PropertyMockSettings extends AbstractSettingsTree<PropertyMockSettings> {

	private final SettingsValue<StringProperty> name;
	/** An example of an immutable property. */
	private final SettingsValue<Property<Path>> path;
	/** An example of a mutable property. */
	private final SettingsValue<Property<Date>> date;
	/** Nested settings object. */
	private final PropertyMockSubSettings sub = new PropertyMockSubSettings();

	public PropertyMockSettings() {
		super("MockSettings");
		// Property is mutable, we need to use MutableSettingsValue everywhere
		name = newNode(new MutableSettingsValue<StringProperty>(
				"name", new SimpleStringProperty(),
				p -> new SimpleStringProperty(p.getValue()),
				p -> p.get(), s-> new SimpleStringProperty(s)));
		path = newNode(new MutableSettingsValue<Property<Path>>(
				"path", new SimpleObjectProperty<>(),
				p -> new SimpleObjectProperty<Path>(p.getValue()),
				p -> p.getValue().toString(),
				s -> new SimpleObjectProperty<Path>(Paths.get(s))));
		date = newNode(new DatePropertySettingsValue(
				"date", new SimpleObjectProperty<>(new Date())));
		newNode(sub);
	}

	@Override
	protected PropertyMockSettings newInstance(String key) {
		return new PropertyMockSettings();
	}

	public String getName() {
		return name.value().get();
	}

	public void setName(String name) {
		this.name.value().set(name);
	}

	public Property<String> nameProperty() {
		return this.name.value();
	}

	public Path getPath() {
		return path.value().getValue();
	}

	public void setPath(Path path) {
		this.path.value().setValue(path);
	}

	public Property<Path> pathProperty() {
		return this.path.value();
	}

	public Date getDate() {
		return date.value().getValue();
	}

	public void setDate(Date date) {
		this.date.value().setValue(date);
	}

	public Property<Date> DateProperty() {
		return this.date.value();
	}

	public PropertyMockSubSettings getNested() {
		return sub;
	}

}
