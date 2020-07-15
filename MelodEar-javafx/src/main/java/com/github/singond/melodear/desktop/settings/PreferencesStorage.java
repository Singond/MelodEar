package com.github.singond.melodear.desktop.settings;

import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.settings.SettingsNode;
import com.github.singond.settings.SettingsNodeVisitor;
import com.github.singond.settings.SettingsTreeNode;
import com.github.singond.settings.SettingsValueNode;

public class PreferencesStorage {

	private static final String SEPARATOR = ".";
	private static final String DEFAULT_STRING = "";

	private static Logger logger
			= LogManager.getLogger(PreferencesStorage.class);

	private final Preferences prefs;
	private final SettingsNodeVisitor writer = new SettingsWriter();
	private final SettingsNodeVisitor reader = new SettingsReader();

	public PreferencesStorage(Preferences preferencesNode) {
		this.prefs = preferencesNode;
	}

	public void writeSettings(SettingsTreeNode<?> settings) {
		settings.invite(writer);
	}

	public void readSettings(SettingsTreeNode<?> settings) {
		settings.invite(reader);
	}

	public void clearUserSettings() {
		try {
			prefs.removeNode();
		} catch (BackingStoreException e) {
			logger.error("Error while clearing user preferences", e);
		}
	}

	public void clearSystemSettings() {
		try {
			prefs.removeNode();
		} catch (BackingStoreException e) {
			logger.error("Error while clearing user preferences", e);
		}
	}

	/**
	 * Constructs the key under which the given node value is stored.
	 *
	 * @param node the node value to be stored or retrieved
	 * @return the key
	 */
	private static String wholeKey(SettingsValueNode<?, ?> node) {
		List<SettingsNode<?>> ancestors = node.ancestors();
		StringBuilder path = new StringBuilder();
		for (SettingsNode<?> n : ancestors) {
			path.append(n.key()).append(SEPARATOR);
		}
		path.append(node.key());
		return path.toString();
	}

	private class SettingsWriter implements SettingsNodeVisitor {

		@Override
		public void visitValue(SettingsValueNode<?, ?> value) {
			String key = wholeKey(value);
			String stringValue = value.valueToString();
			if (key != null && stringValue != null) {
				prefs.put(key, stringValue);
			}
		}

		@Override
		public void visitTree(SettingsTreeNode<?> tree) {
			// skip
		}
	}

	private class SettingsReader implements SettingsNodeVisitor {

		@Override
		public void visitValue(SettingsValueNode<?, ?> value) {
			String str = prefs.get(wholeKey(value), DEFAULT_STRING);
			if (!str.equals(DEFAULT_STRING)) {
				value.setValueFromString(str);
			}
		}

		@Override
		public void visitTree(SettingsTreeNode<?> tree) {
			// skip
		}
	}

}
