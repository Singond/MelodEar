package com.github.singond.melodear.desktop.settings;

public interface SettingsNode<S extends SettingsNode<S>> {

	String key();

	void updateFrom(S src);

}