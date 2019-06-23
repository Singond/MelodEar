package com.github.singond.melodear.desktop;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dagger.Lazy;

import com.github.singond.melodear.desktop.piano.PianoController;
import com.github.singond.melodear.desktop.settings.AllSettings;
import com.github.singond.melodear.desktop.settings.SettingsController;

public class MainController {

	private static Logger logger = LogManager.getLogger(MainController.class);

	@FXML
	private BorderPane main;

	@Inject
	Lazy<PianoController> pianoController;

	@Inject
	AllSettings settings;

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
		ResourceBundle bundle = ResourceBundle.getBundle("loc/settings");
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/view/settings.fxml"), bundle);
		loader.setController(new SettingsController(settings));
		try {
			Dialog<AllSettings> dlg = new Dialog<>();
			dlg.setTitle(bundle.getString("title"));
			DialogPane dlgPane = loader.load();
			dlgPane.getStylesheets().add("/view/settings.css");
			dlg.setDialogPane(dlgPane);
//			dlg.setResultConverter(t -> dlgPane.getS);
			Optional<AllSettings> result = dlg.showAndWait();
//			if (result.isPresent()) {
//				if (result.get() == ButtonType.APPLY) {
//					logger.debug("Applying settings");
//				} else if (result.get() == ButtonType.CANCEL) {
//					logger.debug("Canceled");
//				}
//			}
		} catch (IOException e) {
			logger.error("Error loading settings", e);
		}
	}

	public void exit() {
		logger.info("Exiting");
		Platform.exit();
	}
}
