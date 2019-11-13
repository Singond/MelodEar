package com.github.singond.settings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

public class PathSettingsValue extends ImmutableSettingsValue<Path> {

	private static final Function<Path, String> TO_STRING
			= p -> p.toString();

	public PathSettingsValue(String key, Path value) {
		super(key, value, TO_STRING, Paths::get);
	}

}
