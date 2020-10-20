package com.github.singond.melodear.desktop.trainer;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dagger.Lazy;

import com.github.singond.melodear.MelodyExercise.NoteStatus;
import com.github.singond.melodear.desktop.PaneScoped;
import com.github.singond.melodear.desktop.audio.AudioDevice;
import com.github.singond.melodear.desktop.piano.PianoKeyboardListener;
import com.github.singond.melodear.desktop.settings.AllSettings;
import com.github.singond.music.Pitch;

@PaneScoped
public class MelodyTrainerKeyboardListener extends PianoKeyboardListener {

	private static Logger logger
			= LogManager.getLogger(MelodyTrainerKeyboardListener.class);

	private final MelodyTrainerModel trainer;
	private final Lazy<MelodyTrainerController> trainerController;

	@Inject
	public MelodyTrainerKeyboardListener(AudioDevice audio, AllSettings settings,
			MelodyTrainerModel trainerModel,
			Lazy<MelodyTrainerController> trainerController) {
		super(audio, settings);
		logger.trace("Creating MelodyTrainerListener");
		trainer = trainerModel;
		this.trainerController = trainerController;
	}

	@Override
	protected void onKeyDown(Pitch pitch) {
		super.onKeyDown(pitch);
		NoteStatus status = trainer.evaluate(pitch);
		trainerController.get().noteEvaluated(status);
	}
}
