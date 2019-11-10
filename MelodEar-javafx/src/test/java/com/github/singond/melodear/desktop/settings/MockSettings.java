package com.github.singond.melodear.desktop.settings;

import java.nio.file.Path;
import java.util.Date;

public class MockSettings extends AbstractSettingsTree<MockSettings> {

	private SettingsValue<String,?> name
			= new ImmutableSettingsValue<String>("name", null);
	/** An example of an immutable property. */
	private SettingsValue<Path,?> path
			= new ImmutableSettingsValue<Path>("path", null);
	/** An example of a mutable property. */
	private final SettingsValue<Date,?> date
			= new MutableSettingsValue<Date>("date", null,
					d -> new Date(d.getTime()));
	/** Nested settings object. */
	private final MockSubSettings sub = new MockSubSettings();

	public MockSettings() {
		super("MockSettings");
		addItem(name);
		addItem(path);
		addItem(date);
		addItem(sub);
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

	public Path getPath() {
		return path.value();
	}

	public void setPath(Path path) {
		this.path.setValue(path);
	}

	public Date getDate() {
		return date.value();
	}

	public void setDate(Date date) {
		this.date.setValue(date);
	}

	public MockSubSettings getNested() {
		return sub;
	}

}
