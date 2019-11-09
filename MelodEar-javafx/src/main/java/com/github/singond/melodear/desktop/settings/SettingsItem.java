package com.github.singond.melodear.desktop.settings;

public interface SettingsItem<T, S extends SettingsItem<T, S>> {

	String key();

	T value();

	T valueCopy();

	void updateFrom(S src);

}