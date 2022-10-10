package com.github.singond.melodear.desktop.settings;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.util.StringConverter;

/**
 * A converter between instances of {@link Path} and {@code String}.
 * Note that this converter converts empty string to null instead of
 * a {@code Path} object representing the current directory.
 * This is different from how the {@code Path} class treats empty strings.
 */
public class PathStringConverter extends StringConverter<Path> {

	/**
	 * Converts the provided {@code Path} into its string form.
	 *
	 * @param path the path to be converted
	 * @return the string representation of {@code path}.
	 *         If {@code path} is {@code null}, returns {@code null}.
	 */
	@Override
	public String toString(Path path) {
		if (path == null) {
			return "";
		} else {
			return path.toString();
		}
	}

	/**
	 * Converts the provided string into a {@code Path}.
	 * If the string is empty or {@code null}, returns {@code null}.
	 * Note that this behaviour is different from the {@link Path} class,
	 * where an empty string is treated as the {@code Path} to the current
	 * directory.
	 *
	 * @param string the string to be converted
	 * @return the path represented by {@code string}.
	 *         If {@code string} is {@code null} or empty, returns {@code null}.
	 * @throws InvalidPathException if {@code string} cannot be converted to
	 *         a {@code Path}
	 */
	@Override
	public Path fromString(String string) {
		if (string == null || string.trim().isEmpty()) {
			return null;
		} else {
			return Paths.get(string);
		}
	}

}
