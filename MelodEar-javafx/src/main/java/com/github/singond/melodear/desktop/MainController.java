package com.github.singond.melodear.desktop;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dagger.Lazy;

import com.github.singond.melodear.desktop.piano.PianoController;

public class MainController {

	private static Logger logger = LogManager.getLogger(MainController.class);

	@FXML
	private BorderPane main;

	@Inject
	Lazy<PianoController> pianoController;

	@Inject
	public MainController() {}

	public void initialize() {
		// Activate the default pane
		switchToPiano();
	}

	public void switchToPiano() {
		try {
			main.setCenter(pianoPane());
		} catch (IOException e) {
			logger.error("Error loading piano pane", e);
		}
	}

	private final Parent pianoPane() throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/view/piano/piano.fxml"));
		loader.setController(pianoController.get());
		Parent pane = loader.load();
		pane.getStylesheets().add("/view/piano/piano.css");
		return pane;
	}

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
			logger.error("Error loading settings", e);
		}
	}

	public void exit() {
		logger.info("Exiting");
		Platform.exit();
	}
}
