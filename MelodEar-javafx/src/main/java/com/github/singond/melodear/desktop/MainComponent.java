package com.github.singond.melodear.desktop;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AudioModule.class)
interface MainComponent {

	MainController getMainController();

}
