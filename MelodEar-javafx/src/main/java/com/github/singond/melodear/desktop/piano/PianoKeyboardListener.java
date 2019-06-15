package com.github.singond.melodear.desktop.piano;

import javax.inject.Inject;

import com.github.singond.melodear.desktop.AudioDevice;
import com.github.singond.melodear.desktop.keyboard.AbstractKeyboardListener;
import com.github.singond.melodear.desktop.keyboard.KeyboardSettings;
import com.github.singond.music.Pitch;

public class PianoKeyboardListener extends AbstractKeyboardListener {

	@Inject
	public PianoKeyboardListener(AudioDevice audio, KeyboardSettings settings) {
		super(audio, settings);
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
