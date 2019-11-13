package com.github.singond.melodear.desktop.settings;

import java.util.List;
import java.util.prefs.Preferences;

import com.github.singond.settings.SettingsNode;
import com.github.singond.settings.SettingsNodeVisitor;
import com.github.singond.settings.SettingsTree;
import com.github.singond.settings.SettingsValueNode;

public class PreferencesStorage {

	private final Class<?> cls;
	private final SettingsNodeVisitor userWriter = new UserSettingsWriter();
	private final SettingsNodeVisitor sysWriter = new SystemSettingsWriter();

	public PreferencesStorage(Class<?> cls) {
		this.cls = cls;
	}

	public void writeUserSettings(SettingsTree<?> settings) {
		settings.invite(userWriter);
	}

	public void writeSystemSettings(SettingsTree<?> settings) {
		settings.invite(sysWriter);
	}

	private abstract class SettingsWriter implements SettingsNodeVisitor {

		protected abstract Preferences preferencesNode();

		@Override
		public void visitValue(SettingsValueNode<?, ?> value) {
			List<SettingsNode<?>> ancestors = value.ancestors();
			StringBuilder path = new StringBuilder();
			for (SettingsNode<?> node : ancestors) {
				path.append(node.key()).append(".");
			}
			path.append(value.key());
			// TODO: Use dedicated method, not toString
//			preferencesNode().put(value.key(), value.value().toString());

			System.out.println(path + ": " + value.value());
		}

		@Override
		public void visitTree(SettingsTree<?> tree) {
			// skip
		}
	}

	private class UserSettingsWriter extends SettingsWriter {
		@Override
		protected Preferences preferencesNode() {
			return Preferences.userNodeForPackage(cls);
		}
	}

	private class SystemSettingsWriter extends SettingsWriter {
		@Override
		protected Preferences preferencesNode() {
			return Preferences.systemNodeForPackage(cls);
		}
	}
}
