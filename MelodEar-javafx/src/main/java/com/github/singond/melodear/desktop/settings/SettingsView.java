package com.github.singond.melodear.desktop.settings;

import javafx.scene.Node;

public interface SettingsView<T extends Settings> {

	/**
	 * Returns a node containing all controls for this part of settings.
	 *
	 * @param settingsObj the settings object whose part is to be modified
	 *        by this view
	 * @return a node populated with controls
	 */
	Node getNode(AllSettings settingsObj);

}
