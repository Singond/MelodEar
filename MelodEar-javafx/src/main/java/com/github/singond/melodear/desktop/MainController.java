package com.github.singond.melodear.desktop;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.inject.Provider;

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

import com.github.singond.melodear.desktop.audio.AudioController;
import com.github.singond.melodear.desktop.piano.PianoController;
import com.github.singond.melodear.desktop.settings.AllSettings;
import com.github.singond.melodear.desktop.settings.SettingsControllerComponent;
import com.github.singond.melodear.desktop.trainer.TrainerComponent;

public class MainController {

	private static Logger logger = LogManager.getLogger(MainController.class);

	@FXML
	private BorderPane main;

	@Inject
	Lazy<PianoController> pianoController;

	@Inject
	Provider<TrainerComponent.Builder> trainerProvider;

	@Inject
	AudioController audioController;

	/**
	 * Creates fresh instance of SettingsController based on a copy of
	 * the current settings on every invocation.
	 */
	@Inject
	Provider<SettingsControllerComponent.Builder> settingsControllerProvider;

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
		logger.debug("Opening piano view");
		try {
			Parent pane = pianoPane();
			preSwitchView();
			main.setCenter(pane);
			postSwitchView();
		} catch (IOException e) {
			logger.error("Error loading piano view", e);
		}
	}

	public void switchToMelodyTrainer() {
		logger.debug("Opening melody trainer view");
		try {
			Parent pane = trainerPane();
			preSwitchView();
			main.setCenter(pane);
			postSwitchView();
		} catch (IOException e) {
			logger.error("Error loading melody trainer view", e);
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
		TrainerComponent tc = trainerProvider.get().build();
		loader.setController(tc.getTrainerController());
		Parent pane = loader.load();
		return pane;
	}

	public void openSettings() {
		logger.debug("Opening settings");
		try {
			createSettingsDialog().showAndWait();
		} catch (IOException e) {
			logger.error("Error creating settings dialog", e);
			// TODO: Display error?
			return;
		}
	}

	private Dialog<AllSettings> createSettingsDialog() throws IOException {
		logger.debug("Creating settings dialog");
		ResourceBundle bundle = ResourceBundle.getBundle("loc/settings");
		Dialog<AllSettings> dlg = new Dialog<>();
		dlg.setTitle(bundle.getString("title"));
		dlg.setDialogPane(createSettingsDialogPane());
		return dlg;
	}

	private DialogPane createSettingsDialogPane() throws IOException {
		logger.debug("Creating settings dialog contents");
		ResourceBundle bundle = ResourceBundle.getBundle("loc/settings");
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/view/settings.fxml"), bundle);
		SettingsControllerComponent bldr
				= settingsControllerProvider.get().build();
		loader.setController(bldr.getSettingsController());
		DialogPane dlgPane = loader.load();
		dlgPane.getStylesheets().add("/view/settings.css");
		return dlgPane;
	}

	public void exit() {
		logger.info("Exiting");
		Platform.exit();
	}
}
