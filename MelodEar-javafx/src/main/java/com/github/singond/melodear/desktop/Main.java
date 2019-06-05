package com.github.singond.melodear.desktop;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	private static MidiAudioDevice audio;

	public static MidiAudioDevice getAudioDevice() {
		return audio;
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("MelodEar");
		stage.setScene(pianoScene());
		stage.show();
	}

	private Scene pianoScene() throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/piano/piano.fxml"));
		Parent root = loader.load();
//		PianoController ctrl = loader.getController();

		Scene scene = new Scene(root);
		scene.getStylesheets().add("/piano/piano.css");
		return scene;
	}

	public static void main(String[] args) {
		try {
			audio = new MidiAudioDevice();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		launch(args);
	}
}