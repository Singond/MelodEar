package com.github.singond.melodear.desktop;

import javax.inject.Singleton;

import dagger.Component;

import com.github.singond.melodear.desktop.audio.AudioModule;
import com.github.singond.melodear.desktop.settings.SettingsModule;
import com.github.singond.melodear.desktop.trainer.TrainerModule;

@Singleton
@Component(modules = {AudioModule.class, SettingsModule.class, TrainerModule.class})
interface MainComponent {

	MainController getMainController();

}
