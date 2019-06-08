package com.github.singond.melodear.desktop;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("MelodEar");
		stage.setScene(pianoScene());
		stage.show();
	}

	private Scene pianoScene() throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/piano/piano.fxml"));
		loader.setController(DaggerMainComponent.create().getPianoController());
		Parent root = loader.load();

		Scene scene = new Scene(root);
		scene.getStylesheets().add("/piano/piano.css");
		return scene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}