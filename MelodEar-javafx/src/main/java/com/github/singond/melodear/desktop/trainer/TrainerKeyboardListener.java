package com.github.singond.melodear.desktop.trainer;

import javax.inject.Inject;

import com.github.singond.melodear.KeyedMelodyExercise;
import com.github.singond.melodear.MelodyExercise.NoteEvaluationStatus;
import com.github.singond.melodear.MelodyTrainer;
import com.github.singond.melodear.desktop.audio.AudioDevice;
import com.github.singond.melodear.desktop.piano.PianoKeyboardListener;
import com.github.singond.melodear.desktop.settings.AllSettings;
import com.github.singond.music.Pitch;

public class TrainerKeyboardListener extends PianoKeyboardListener {

	private final MelodyTrainer<KeyedMelodyExercise> trainer;

	@Inject
	public TrainerKeyboardListener(AudioDevice audio, AllSettings settings,
			TrainerModel trainerModel) {
		super(audio, settings);
		trainer = trainerModel.trainer();
	}

	@Override
	protected void onKeyDown(Pitch pitch) {
		super.onKeyDown(pitch);
		if (trainer.hasExercise()) {
			NoteEvaluationStatus status = trainer.getExercise().evaluate(pitch);
		}
	}

}
