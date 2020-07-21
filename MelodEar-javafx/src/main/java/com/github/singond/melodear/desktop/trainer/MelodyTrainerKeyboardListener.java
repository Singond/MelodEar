package com.github.singond.melodear.desktop.trainer;

import javax.inject.Inject;

import com.github.singond.melodear.desktop.audio.AudioDevice;
import com.github.singond.melodear.desktop.piano.PianoKeyboardListener;
import com.github.singond.melodear.desktop.settings.AllSettings;
import com.github.singond.music.Pitch;

public class MelodyTrainerKeyboardListener extends PianoKeyboardListener {

	private final MelodyTrainerModel trainer;

	@Inject
	public MelodyTrainerKeyboardListener(AudioDevice audio, AllSettings settings,
			MelodyTrainerModel trainerModel) {
		super(audio, settings);
		trainer = trainerModel;
	}

	@Override
	protected void onKeyDown(Pitch pitch) {
		super.onKeyDown(pitch);
		trainer.evaluate(pitch);
	}
}
