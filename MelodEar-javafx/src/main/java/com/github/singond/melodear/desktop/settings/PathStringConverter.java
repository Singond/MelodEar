package com.github.singond.melodear.desktop.settings;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.util.StringConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A converter between instances of {@code Path} and {@code String}.
 *
 * @author Singon
 */
public class PathStringConverter extends StringConverter<Path> {

	private static Logger logger
			= LogManager.getLogger(PathStringConverter.class);

	/**
	 * Converts the provided {@code Path} into its string form.
	 *
	 * @param path the path to be converted
	 * @return the string representation of {@code path}.
	 *         If {@code path} is {@code null}, returns {@code null}.
	 */
	@Override
	public String toString(Path path) {
		logger.debug("Converting path '{}' to string", path);
		if (path == null) {
			return null;
		} else {
			return path.toString();
		}
	}

	/**
	 * Converts the provided string into a {@code Path}.
	 *
	 * @param string the string to be converted
	 * @return the path represented by {@code string}.
	 *         If {@code string} is {@code null}, returns {@code null}.
	 * @throws InvalidPathException if {@code string} cannot be converted to
	 *         a {@code Path}
	 */
	@Override
	public Path fromString(String string) {
		logger.debug("Converting string '{}' to path", string);
		if (string == null) {
			return null;
		} else {
			return Paths.get(string);
		}
	}

}
