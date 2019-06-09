package com.github.singond.melodear.desktop;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;

public class MainController {

	private static Logger logger = LogManager.getLogger(MainController.class);

	@FXML
	private BorderPane main;

	@Inject
	public MainController() {}

	public void openSettings() {
		logger.debug("Opening settings");
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/view/settings.fxml"));
		try {
			Dialog<ButtonType> dlg = new Dialog<>();
			DialogPane dlgPane = loader.load();
			dlg.setDialogPane(dlgPane);
			Optional<ButtonType> result = dlg.showAndWait();
			if (result.isPresent()) {
				if (result.get() == ButtonType.APPLY) {
					logger.debug("Applying settings");
				} else if (result.get() == ButtonType.CANCEL) {
					logger.debug("Canceled");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void exit() {
		logger.info("Exiting");
		Platform.exit();
	}
}
