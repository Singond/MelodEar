package com.github.singond.melodear.desktop;

import javax.inject.Singleton;

import com.github.singond.melodear.desktop.piano.PianoController;

import dagger.Component;

@Singleton
@Component(modules = AudioModule.class)
interface MainComponent {

	MainController getMainController();
	PianoController getPianoController();

}
