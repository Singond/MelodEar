package com.github.singond.melodear.desktop.keyboard;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import com.github.singond.melodear.desktop.settings.AllSettings;
import com.github.singond.melodear.desktop.settings.SettingsView;

public class KeyboardSettingsView implements SettingsView<KeyboardSettings> {

	private static final ResourceBundle bundle
			= ResourceBundle.getBundle("loc/settings");

	@Override
	public Node getNode(AllSettings s) {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/view/settings_keyboard.fxml"), bundle);
		loader.setController(new KeyboardSettingsController(s.keyboard()));
		try {
			Parent node = (Parent) loader.load();
			node.getStylesheets().add("/view/settings_keyboard.css");
			return node;
		} catch (IOException e) {
			throw new RuntimeException("Could not load view/settings_keyboard.fxml", e);
		}
	}

	@Override
	public String getName() {
		return bundle.getString("keyboard.title");
	}

}
