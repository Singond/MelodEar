package com.github.singond.melodear.desktop;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.audio.AudioException;

/**
 * The main class of the application.
 *
 * @author Singon
 */
public class MelodEarApplication extends Application {

	private static final List<String> iconNames = Arrays.asList(
			"melodear-clefear-48.png",
			"melodear-clefear-256.png"
			);
	private static final String iconPrefix = "/img/";

	private static Logger logger = LogManager.getLogger(MelodEarApplication.class);

	private MainComponent component;

	public MelodEarApplication() {
		component = DaggerMainComponent.create();
	}

	@Override
	public void start(Stage stage) throws Exception {
		URL fxml = getClass().getResource("/view/main.fxml");
		ResourceBundle bundle = ResourceBundle.getBundle("loc/main");
		FXMLLoader loader = new FXMLLoader(fxml, bundle);
		loader.setController(component.getMainController());
		BorderPane root = (BorderPane) loader.load();
		Scene scene = new Scene(root);

		stage.setTitle("MelodEar");
		stage.getIcons().addAll(initIcons());
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() {
		try {
			component.getAudioDevice().muteAll();
		} catch (AudioException e) {
			logger.error("Exception when muting sound", e);
		}
		logger.debug("Writing settings");
		component.getSettingsLoader().writeSettings();
	}

	private List<Image> initIcons() {
		List<Image> icons = new ArrayList<>(iconNames.size());
		for (String name : iconNames) {
			try (InputStream iconRes = MelodEarApplication.class
					.getResourceAsStream(iconPrefix + name)) {
				if (iconRes != null) {
					icons.add(new Image(iconRes));
				}
			} catch (IOException e) {
				logger.error("Error opening icon file", e);
			}
		}
		return icons;
	}

	public static void main(String[] args) {
		launch(args);
	}
}