package com.github.singond.melodear.desktop;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/view/main.fxml"));
		BorderPane root = (BorderPane) loader.load();
		Scene scene = new Scene(root);
		root.setCenter(pianoPane());

		stage.setTitle("MelodEar");
		stage.setScene(scene);
		stage.show();
	}

	private Parent pianoPane() throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/view/piano/piano.fxml"));
		loader.setController(DaggerMainComponent.create().getPianoController());
		Parent pane = loader.load();
		pane.getStylesheets().add("/view/piano/piano.css");
		return pane;
	}

	public static void main(String[] args) {
		launch(args);
	}
}