package com.github.singond.melodear.desktop.settings;

import java.nio.file.Path;
import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MockSettings extends AbstractSettings<MockSettings> {

	private final StringProperty name = new SimpleStringProperty();
	/** An example of an immutable property. */
	private final ObjectProperty<Path> path = new SimpleObjectProperty<>();
	/** An example of a mutable property. */
	private final ObjectProperty<Date> date = new SimpleObjectProperty<>(new Date());

	public MockSettings() {
		addItem(new BasicSettingsItem<String>("name", name));
		addItem(new BasicSettingsItem<Path>("path", path));
		// Date is mutable, we need to create a defensive copy on duplicating
		addItem(new BasicSettingsItem<Date>("date", date,
				d -> new Date(d.getTime())));
	}

	@Override
	public MockSettings copy() {
		MockSettings copy = new MockSettings();
		copy.updateFrom(this);
		return copy;
	}

	@Override
	protected MockSettings newInstance() {
		return new MockSettings();
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
		return path.get();
	}

	public void setPath(Path path) {
		this.path.set(path);
	}

	public Property<Path> pathProperty() {
		return this.path;
	}

	public Date getDate() {
		return date.get();
	}

	public void setDate(Date date) {
		this.date.set(date);
	}

	public Property<Date> DateProperty() {
		return this.date;
	}

}
