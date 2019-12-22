package com.github.singond.melodear.desktop.trainer;

import javax.inject.Inject;

import com.github.singond.melodear.desktop.audio.AudioDevice;
import com.github.singond.melodear.desktop.piano.PianoKeyboardListener;
import com.github.singond.melodear.desktop.settings.AllSettings;

public class TrainerKeyboardListener extends PianoKeyboardListener {

	@Inject
	public TrainerKeyboardListener(AudioDevice audio, AllSettings settings) {
		super(audio, settings);
	}

}
