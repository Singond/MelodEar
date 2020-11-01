package com.github.singond.melodear.desktop;

import java.util.ResourceBundle;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

	@Provides @Singleton @Named("main-resources")
	public static ResourceBundle provideResourceBundle() {
		return ResourceBundle.getBundle("loc/main");
	}
}
