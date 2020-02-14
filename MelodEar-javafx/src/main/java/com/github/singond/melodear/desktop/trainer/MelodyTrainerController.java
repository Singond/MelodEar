package com.github.singond.melodear.desktop.trainer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.KeyedMelodyExercise;
import com.github.singond.melodear.MelodyTrainer;
import com.github.singond.melodear.desktop.audio.AudioDevice;
import com.github.singond.melodear.desktop.components.Keyboard;
import com.github.singond.melodear.desktop.keyboard.KeyboardSettings;
import com.github.singond.melodear.desktop.settings.AllSettings;
import com.github.singond.music.Chords;
import com.github.singond.music.Key;
import com.github.singond.music.Pitch;
import com.github.singond.music.PitchGroup;
import com.github.singond.music.SimpleInterval;

public class MelodyTrainerController {

	private static Logger logger = LogManager.getLogger(MelodyTrainerController.class);

	private MelodyTrainerModel trainerModel;

	@Inject
	AudioDevice audio;

	@Inject
	Provider<TrainerComponent.Builder> trainerProvider;

	KeyboardSettings kbdSettings;

	@FXML
	private Keyboard keyboard;

	@FXML
	private Button startBtn;

	@FXML
	private Button replayReferenceBtn;

	@FXML
	private Button replayMelodyBtn;

	@Inject
	public MelodyTrainerController(AllSettings settings) {
		kbdSettings = settings.keyboard();
	}

	public void initialize() {
		logger.debug("Setting keyboard listener");
		keyboard.setLabelFormat(kbdSettings.getKeyLabelFormat());
		kbdSettings.keyLabelFormatProperty().addListener((v, o, n) -> {
			logger.debug("Setting label format");
			keyboard.setLabelFormat(n);
		});
		initTrainer();
		startBtn.setDisable(false);
		replayReferenceBtn.setDisable(true);
		replayMelodyBtn.setDisable(true);
	}

	private void initTrainer() {
		TrainerComponent trainerComp = trainerProvider.get().build();
		trainerModel = trainerComp.getTrainerModel();
		keyboard.setListener(trainerComp.getTrainerKeyboardListener());
	}

	private MelodyTrainer<KeyedMelodyExercise> trainer() {
		final MelodyTrainerModel tm = trainerModel;
		if (tm == null) {
			throw new IllegalStateException("Trainer has not been initialized");
		}
		return tm.trainer();
	}

	public void startExercise() {
		logger.debug("Starting exercise");
		startBtn.setDisable(true);
		replayReferenceBtn.setDisable(false);
		replayMelodyBtn.setDisable(false);
		trainer().newExercise();
	}

	public void replayReference() {
		logger.debug("'Replay reference' pressed");
		final Key key = trainer().getExercise().key();
		Pitch tonic = Pitch.of(key.tonic(), 4);
		List<PitchGroup> voice = new ArrayList<>(3);
		voice.add(Chords.chordAtRoot(tonic, Chords.MAJOR_TRIAD));
		voice.add(Chords.chordAtRoot(
				tonic.transposeUp(SimpleInterval.PERFECT_FOURTH),
				Chords.MAJOR_TRIAD.invert(2)));
		voice.add(Chords.chordAtRoot(
				tonic.transposeUp(SimpleInterval.PERFECT_FIFTH),
				Chords.MAJOR_TRIAD.invert(1)));
		audio.playSequentially(voice, 120);
	}

	public void replayMelody() {
		logger.debug("Replaying melody");
		audio.playSequentially(trainer().getExercise().melody(), 120);
	}

	public void playChord() {
		List<PitchGroup> voice = new ArrayList<>(3);
		voice.add(Chords.chordAtRoot(Pitch.D4, Chords.MAJOR_TRIAD));
		audio.playSequentially(voice, 120);
	}

	public void playChordProgression() {
		List<PitchGroup> voice = new ArrayList<>(3);
		voice.add(Chords.chordAtRoot(Pitch.D4, Chords.MAJOR_TRIAD));
		voice.add(Chords.chordAtRoot(Pitch.G4, Chords.MAJOR_TRIAD.invert(2)));
		voice.add(Chords.chordAtRoot(Pitch.A4, Chords.MAJOR_TRIAD.invert(1)));
		audio.playSequentially(voice, 120);
	}
}
