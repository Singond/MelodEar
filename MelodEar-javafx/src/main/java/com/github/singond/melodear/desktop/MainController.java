package com.github.singond.melodear.desktop;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class MainController {

	private static Logger logger = LogManager.getLogger(MainController.class);

	@FXML
	private BorderPane main;

	@Inject
	public MainController() {}

	public void exit() {
		logger.info("Exiting");
		Platform.exit();
	}
}
