package com.github.singond.melodear.desktop;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The main class of the application.
 *
 * @author Singon
 */
public class MelodEarApplication extends Application {

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
		scene.getStylesheets().add("/view/main.css");

		stage.setTitle("MelodEar");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}