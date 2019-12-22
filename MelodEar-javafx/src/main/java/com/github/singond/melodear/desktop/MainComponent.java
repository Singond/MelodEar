package com.github.singond.melodear.desktop;

import javax.inject.Singleton;

import dagger.Component;

import com.github.singond.melodear.desktop.audio.AudioModule;
import com.github.singond.melodear.desktop.settings.SettingsModule;

@Singleton
@Component(modules = {AudioModule.class, SettingsModule.class})
interface MainComponent {

	MainController getMainController();

}
