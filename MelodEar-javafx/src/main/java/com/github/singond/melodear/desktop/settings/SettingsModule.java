package com.github.singond.melodear.desktop.settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = {SettingsControllerComponent.class})
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
