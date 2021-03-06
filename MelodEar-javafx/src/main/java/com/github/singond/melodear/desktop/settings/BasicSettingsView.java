package com.github.singond.melodear.desktop.settings;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import com.github.singond.settings.SettingsTreeNode;

public abstract class BasicSettingsView<T extends SettingsTreeNode<?>>
		implements SettingsView<T> {

	protected final String id;
	protected final ResourceBundle bundle;
	private transient String uiName;
	private transient String intName;

	protected BasicSettingsView(String id) {
		this.id = id;
		this.bundle = ResourceBundle.getBundle("loc/settings");
		this.uiName = bundle.getString(id + ".title");
		this.intName = id + " settings";
	}

	protected BasicSettingsView(String id, ResourceBundle bundle) {
		this.id = id;
		this.bundle = bundle;
		this.uiName = bundle.getString(id + ".settings.title");
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
