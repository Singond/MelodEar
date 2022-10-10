package com.github.singond.melodear.desktop.trainer;

import java.util.ResourceBundle;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = {TrainerComponent.class})
public class TrainerModule {

	@Provides @Singleton @Named("trainer-resources")
	public static ResourceBundle provideResourceBundle() {
		return ResourceBundle.getBundle("loc/trainer");
	}
}
