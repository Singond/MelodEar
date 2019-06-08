package com.github.singond.melodear.desktop;

import com.github.singond.melodear.desktop.piano.PianoController;

import dagger.Component;

@Component
public interface MainComponent {

	PianoController getPianoController();
}
