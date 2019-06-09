package com.github.singond.melodear.desktop;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The enclosing class of the application model.
 *
 * @author Singon
 */
public class Main {

	private static Logger logger = LogManager.getLogger(Main.class);

	@Inject
	AudioDevice audio;

	@Inject
	public Main () {
		logger.debug("Instantiating Main");
	}
}
