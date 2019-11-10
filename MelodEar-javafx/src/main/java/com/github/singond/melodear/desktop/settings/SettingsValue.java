package com.github.singond.melodear.desktop.settings;

public interface SettingsValue<T, S extends SettingsValue<T, S>> extends SettingsNode<S> {

	T value();

	T valueCopy();

}