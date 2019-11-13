package com.github.singond.melodear.desktop.settings;

import java.nio.file.Path;
import java.util.Date;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.converter.DefaultStringConverter;

public class PropertyMockSettings2
		extends PropertyAbstractSettingsTree<PropertyMockSettings2> {

	private final StringProperty name;
	/** An example of an immutable property. */
	private final Property<Path> path;
	/** An example of a mutable property. */
	private final Property<Date> date;
	/** Nested settings object. */
	private final PropertyMockSubSettings2 sub = new PropertyMockSubSettings2();

	public PropertyMockSettings2() {
		super("MockSettings");
		name = newPropertyNode("name", new SimpleStringProperty(),
				new DefaultStringConverter());
		path = newPropertyNode("path", new SimpleObjectProperty<Path>(),
				new PathStringConverter());
		date = newPropertyNode("date", new SimpleObjectProperty<Date>(),
				d -> new Date(d.getTime()), new DateStringConverter());
		newNode(sub);
	}

	@Override
	protected PropertyMockSettings2 newInstance() {
		return new PropertyMockSettings2();
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public Property<String> nameProperty() {
		return this.name;
	}

	public Path getPath() {
		return path.getValue();
	}

	public void setPath(Path path) {
		this.path.setValue(path);
	}

	public Property<Path> pathProperty() {
		return this.path;
	}

	public Date getDate() {
		return date.getValue();
	}

	public void setDate(Date date) {
		this.date.setValue(date);
	}

	public Property<Date> DateProperty() {
		return this.date;
	}

	public PropertyMockSubSettings2 getNested() {
		return sub;
	}

}
