package com.github.singond.settings;

import java.nio.file.Path;
import java.util.Date;

public class MockSettings extends AbstractSettingsTree<MockSettings> {

	private final SettingsValue<String> name;
	/** An example of an immutable property. */
	private final SettingsValue<Path> path;
	/** An example of a mutable property. */
	private final SettingsValue<Date> date;
	/** Nested settings object. */
	private final MockSubSettings sub = new MockSubSettings();

	public MockSettings() {
		super("MockSettings");
		name = newNode(new ImmutableSettingsValue<String>("name", null));
		path = newNode(new ImmutableSettingsValue<Path>("path", null));
		date = newNode(new MutableSettingsValue<Date>("date", null,
				d -> new Date(d.getTime())));
		newNode(sub);
	}

	@Override
	protected MockSettings newInstance() {
		return new MockSettings();
	}

	public String getName() {
		return name.value();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	SettingsNode<?> nameNode() {
		return (SettingsNode<?>) name;
	}

	public Path getPath() {
		return path.value();
	}

	public void setPath(Path path) {
		this.path.setValue(path);
	}

	SettingsNode<?> pathNode() {
		return (SettingsNode<?>) path;
	}

	public Date getDate() {
		return date.value();
	}

	public void setDate(Date date) {
		this.date.setValue(date);
	}

	SettingsNode<?> dateNode() {
		return (SettingsNode<?>) date;
	}

	public MockSubSettings getNested() {
		return sub;
	}

}
