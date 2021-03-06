package com.github.singond.melodear.desktop.piano;

import javax.inject.Inject;

import com.github.singond.melodear.desktop.audio.AudioDevice;
import com.github.singond.melodear.desktop.keyboard.AbstractKeyboardListener;
import com.github.singond.melodear.desktop.settings.AllSettings;
import com.github.singond.music.Pitch;

public class PianoKeyboardListener extends AbstractKeyboardListener {

	@Inject
	public PianoKeyboardListener(AudioDevice audio, AllSettings settings) {
		super(audio, settings.keyboard());
	}

	@Override
	protected void onKeyDown(Pitch pitch) {
		// Do nothing
	}

	@Override
	protected void onKeyUp(Pitch pitch) {
		// Do nothing
	}

}
