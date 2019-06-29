package com.github.singond.melodear.desktop.settings;

import javafx.scene.Node;

public interface SettingsView<T extends Settings> {

	/**
	 * Returns a node containing all controls for this part of settings.
	 *
	 * @return a node populated with controls
	 */
	Node getNode();

}
