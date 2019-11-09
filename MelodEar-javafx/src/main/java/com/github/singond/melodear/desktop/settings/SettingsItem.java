package com.github.singond.melodear.desktop.settings;

import javafx.beans.property.Property;

public interface SettingsItem<T, S extends SettingsItem<T, S>> {

	String key();

	T value();

	T valueCopy();

	Property<T> property();

	void updateFrom(S src);

}