package com.github.singond.melodear.desktop.settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingsModule {

	@Provides @Singleton
	public static SettingsLoader provideSettingsLoader() {
		return new SettingsLoader();
	}

	@Provides
	public static AllSettings provideSettings(SettingsLoader loader) {
		return loader.getSettings();
	}

}
