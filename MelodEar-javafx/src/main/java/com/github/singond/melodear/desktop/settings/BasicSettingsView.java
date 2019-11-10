package com.github.singond.melodear.desktop.settings;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

public abstract class BasicSettingsView <T extends SettingsTree>
		implements SettingsView<T> {

	protected static final ResourceBundle bundle
			= ResourceBundle.getBundle("loc/settings");

	protected final String id;
	private transient String uiName;
	private transient String intName;

	protected BasicSettingsView(String id) {
		this.id = id;
		this.uiName = bundle.getString(id + ".title");
		this.intName = id + " settings";
	}

	@Override
	public Node getNode(AllSettings s) {
		String fxml = String.format("/view/settings_%s.fxml", id);
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(fxml), bundle);
		loader.setController(makeController(s));
		try {
			Parent node = (Parent) loader.load();
			String css = String.format("/view/settings_%s.css", id);
			node.getStylesheets().add(css);
			return node;
		} catch (IOException e) {
			throw new RuntimeException("Could not load " + fxml, e);
		}
	}

	protected abstract Object makeController(AllSettings settings);

	@Override
	public String getName() {
		return uiName;
	}

	@Override
	public String toString() {
		return intName;
	}

}
