package com.github.singond.melodear.desktop.trainer;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private KeyFormatter keyFormatter;
	private ReadOnlyBooleanWrapper running;

	@Inject
	AudioDevice audio;

	@Inject
	Provider<TrainerComponent.Builder> trainerProvider;

	@Inject @Named("trainer-resources")
	ResourceBundle bundle;

	KeyboardSettings kbdSettings;

	@FXML private Keyboard keyboard;
	@FXML private Label keyName;
	@FXML private Label noteStatus;
	@FXML private Label progressNumeric;
	@FXML private Button startBtn;
	@FXML private Button replayReferenceBtn;
	@FXML private Button replayMelodyBtn;

	@Inject
	public MelodyTrainerController(AllSettings settings) {
		kbdSettings = settings.keyboard();
		running = new ReadOnlyBooleanWrapper(false);
		keyFormatter = new KeyFormatter();
	}

	public void initialize() {
		logger.debug("Setting keyboard listener");
		keyboard.setLabelFormat(kbdSettings.getKeyLabelFormat());
		kbdSettings.keyLabelFormatProperty().addListener((v, o, n) -> {
			logger.debug("Setting label format");
			keyboard.setLabelFormat(n);
		});
		initTrainer();
		keyName.textProperty().bind(Bindings.createStringBinding(
				() -> keyFormatter.format(trainerModel.getMelodyKey()),
				trainerModel.melodyKeyProperty()));
		progressNumeric.textProperty().bind(Bindings.format(
				bundle.getString("keymel.progress"),
				trainerModel.notesIdentifiedProperty(),
				trainerModel.melodyLengthProperty()));
		startBtn.disableProperty().bind(running.getReadOnlyProperty());
		replayReferenceBtn.disableProperty().bind(
				Bindings.not(running.getReadOnlyProperty()));
		replayMelodyBtn.disableProperty().bind(
				Bindings.not(running.getReadOnlyProperty()));
	}

	private void initTrainer() {
		TrainerComponent trainerComp = trainerProvider.get().build();
		trainerModel = trainerComp.getTrainerModel();
		keyboard.setListener(trainerComp.getTrainerKeyboardListener());
	}

	public void startExercise() {
		logger.debug("Starting exercise");
		running.set(true);
		trainerModel.newExercise();
	}

	public void replayReference() {
		logger.debug("'Replay reference' pressed");
		final Key key = trainerModel.getMelodyKey();
		Pitch tonic = Pitch.of(key.tonic(), 4);
		List<PitchGroup> voice = new ArrayList<>(3);
		voice.add(Chords.chordAtRoot(tonic, Chords.MAJOR_TRIAD));
		voice.add(Chords.chordAtRoot(
				tonic.transposeUp(SimpleInterval.PERFECT_FOURTH),
				Chords.MAJOR_TRIAD.invert(2)));
		voice.add(Chords.chordAtRoot(
				tonic.transposeUp(SimpleInterval.PERFECT_FIFTH),
				Chords.MAJOR_TRIAD.invert(1)));
		voice.add(Chords.chordAtRoot(tonic, Chords.MAJOR_TRIAD));
		audio.playSequentially(voice, 120);
	}

	public void replayMelody() {
		logger.debug("Replaying melody");
		audio.playSequentially(trainerModel.getMelody(), 120);
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
		voice.add(Chords.chordAtRoot(Pitch.D4, Chords.MAJOR_TRIAD));
		audio.playSequentially(voice, 120);
	}

	private static class KeyFormatter {
		public String format(Key key) {
			if (key == null) {
				return "";
			}
			return key.toString();
		}
	}
}
