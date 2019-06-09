package com.github.singond.melodear.desktop;

import javax.inject.Singleton;

import dagger.Component;

import com.github.singond.melodear.desktop.piano.PianoController;

@Singleton
@Component(modules = AudioModule.class)
interface MainComponent {

	MainController getMainController();
	PianoController getPianoController();

}
