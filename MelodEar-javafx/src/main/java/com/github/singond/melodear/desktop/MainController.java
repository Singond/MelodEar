package com.github.singond.melodear.desktop;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dagger.Lazy;

import com.github.singond.melodear.desktop.piano.PianoController;
import com.github.singond.melodear.desktop.settings.AllSettings;
import com.github.singond.melodear.desktop.settings.SettingsController;
import com.github.singond.melodear.desktop.settings.SettingsLoader;
import com.github.singond.melodear.desktop.trainer.TrainerController;

public class MainController {

	private static Logger logger = LogManager.getLogger(MainController.class);

	@FXML
	private BorderPane main;

	@Inject
	Lazy<PianoController> pianoController;

	@Inject
	Lazy<TrainerController> trainerController;

	@Inject
	SettingsLoader settingsLoader;

	@Inject
	public MainController() {}

	public void initialize() {
		// Activate the default pane
		switchToPiano();
	}

	private void preSwitchView() {}

	private void postSwitchView() {
		Scene scene = main.getScene();
		Window window = scene == null ? null : scene.getWindow();
		if (window != null) {
			window.sizeToScene();
		}
	}

	public void switchToPiano() {
		try {
			Parent pane = pianoPane();
			preSwitchView();
			main.setCenter(pane);
			postSwitchView();
		} catch (IOException e) {
			logger.error("Error loading piano view", e);
		}
	}

	public void switchToTrainer() {
		try {
			Parent pane = trainerPane();
			preSwitchView();
			main.setCenter(pane);
			postSwitchView();
		} catch (IOException e) {
			logger.error("Error loading trainer view", e);
		}
	}

	private final Parent pianoPane() throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/view/piano/piano.fxml"));
		loader.setController(pianoController.get());
		Parent pane = loader.load();
		return pane;
	}

	private final Parent trainerPane() throws IOException {
		ResourceBundle bundle = ResourceBundle.getBundle("loc/trainer");
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/view/trainer/keymel.fxml"), bundle);
		loader.setController(trainerController.get());
		Parent pane = loader.load();
		return pane;
	}

	public void openSettings() {
		logger.debug("Opening settings");
		ResourceBundle bundle = ResourceBundle.getBundle("loc/settings");
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/view/settings.fxml"), bundle);
		loader.setController(new SettingsController(settingsLoader));
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
