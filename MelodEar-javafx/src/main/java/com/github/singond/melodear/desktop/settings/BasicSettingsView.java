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
	private final String nameKey;
	private final String intName;
	private final String fxmlPath;
	private final String cssPath;

	protected BasicSettingsView(String id, String fxmlPath, String cssPath,
			ResourceBundle bundle, String nameKey) {
		this.id = id;
		this.bundle = bundle;
		this.nameKey = nameKey;
		this.fxmlPath = fxmlPath;
		this.cssPath = cssPath;
		this.intName = id + " settings";
	}

	protected BasicSettingsView(String id, String resPath,
			ResourceBundle bundle, String nameKey) {
		this(id, resPath + ".fxml", resPath + ".css", bundle, nameKey);
	}

	protected BasicSettingsView(String id, String resPath,
			ResourceBundle bundle) {
		this(id, resPath, bundle, id + ".settings.title");
	}

	protected BasicSettingsView(String id, String resPath) {
		this(id, resPath, ResourceBundle.getBundle("loc/" + id),
				id + ".settings.title");
	}

	protected BasicSettingsView(String id) {
		this(id, "/view/" + id);
	}

	@Override
	public Node getNode(AllSettings s) {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(fxmlPath), bundle);
		loader.setController(makeController(s));
		try {
			Parent node = (Parent) loader.load();
			node.getStylesheets().add(cssPath);
			return node;
		} catch (IOException e) {
			throw new RuntimeException("Could not load " + fxmlPath, e);
		}
	}

	protected abstract Object makeController(AllSettings settings);

	@Override
	public String getName() {
		return bundle.getString(nameKey);
	}

	@Override
	public String toString() {
		return intName;
	}

}
