package com.github.singond.melodear.desktop.trainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.MelodyExercise.Status;
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

	@Inject
	AudioDevice audio;

	@Inject
	Provider<TrainerComponent.Builder> trainerProvider;

	@Inject @Named("trainer-resources")
	ResourceBundle bundle;

	KeyboardSettings kbdSettings;

	@FXML private Keyboard keyboard;
	@FXML private Label keyName;
	@FXML private Label keyNameLabel;
	@FXML private Label noteStatus;
	@FXML private Label progressNumeric;
	@FXML private Label progressNumericLabel;
	@FXML private Button startBtn;
	@FXML private Button replayReferenceBtn;
	@FXML private Button replayMelodyBtn;

	@Inject
	public MelodyTrainerController(AllSettings settings) {
		kbdSettings = settings.keyboard();
		keyFormatter = new KeyFormatter();
	}

	/**
	 * Initializes this controller.
	 * If the controller is reused between views, this method is called
	 * every time a new view is built.
	 */
	public void initialize() {
		logger.debug("Initializing melody trainer controller.");
		keyboard.setLabelFormat(kbdSettings.getKeyLabelFormat());
		kbdSettings.keyLabelFormatProperty().addListener((v, o, n) -> {
			logger.debug("Setting label format");
			keyboard.setLabelFormat(n);
		});
		initTrainer();
		keyName.textProperty().bind(Bindings.createStringBinding(
				() -> keyFormatter.format(trainerModel.getMelodyKey()),
				trainerModel.melodyKeyProperty()));
		keyNameLabel.setText(bundle.getString("keymel.key") + ": ");
		noteStatus.textProperty().bind(Bindings.createStringBinding(
				this::noteStatusString, trainerModel.statusProperty()));
		progressNumeric.textProperty().bind(Bindings.format(
				bundle.getString("keymel.progress"),
				trainerModel.notesIdentifiedProperty(),
				trainerModel.melodyLengthProperty()));
		progressNumericLabel.setText(
				bundle.getString("keymel.progress_label") + ": ");
		startBtn.textProperty().bind(Bindings.createStringBinding(
				() -> bundle.getString(trainerModel.isRunning()
						? "keymel.stop_trainer"
						: "keymel.start_trainer"),
				trainerModel.runningProperty()));
		replayReferenceBtn.disableProperty().bind(
				Bindings.not(trainerModel.runningProperty()));
		replayMelodyBtn.disableProperty().bind(
				Bindings.not(trainerModel.runningProperty()));
	}

	private void initTrainer() {
		TrainerComponent trainerComp = trainerProvider.get().build();
		trainerModel = trainerComp.getTrainerModel();
		// An exercise may still be present if returning from another pane;
		// stop it now.
		trainerModel.stop();
		logger.debug("Setting keyboard listener.");
		keyboard.setListener(trainerComp.getTrainerKeyboardListener());
	}

	private String noteStatusString() {
		Status status = trainerModel.getStatus();
		if (status == null) {
			return "";
		}
		String result = status.toString().toLowerCase().replace("_", " ");
		return result.substring(0, 1).toUpperCase() + result.substring(1);
	}

	public void startStopExercise() {
		logger.debug("'Start/stop exercise' pressed.");
		if (trainerModel.isRunning()) {
			// Stop
			trainerModel.stop();
		} else {
			// Start
			trainerModel.start();
		}
	}

	public void replayReference() {
		logger.debug("'Replay reference' pressed");
		playKey();
	}

	private void playKey() {
		audio.playSequentially(cadence(), tempo());
	}

	public void replayMelody() {
		logger.debug("Replaying melody");
		playMelody();
	}

	private void playMelody() {
		audio.playSequentially(trainerModel.getMelody(), tempo());
	}

	private void playKeyAndMelody() {
		List<Pitch> melody = trainerModel.getMelody();
		List<PitchGroup> cadence = cadence();
		List<PitchGroup> voice
				= new ArrayList<>(melody.size() + cadence.size() + 1);
		voice.addAll(cadence);
		voice.add(new Rest());
		voice.addAll(melody);
		audio.playSequentially(voice, tempo());
	}

	private List<PitchGroup> cadence() {
		final Key key = trainerModel.getMelodyKey();
		final int startOctave = 4;
		Pitch tonic = Pitch.of(key.tonic(), startOctave);
		List<PitchGroup> cadence = new ArrayList<>(4);
		cadence.add(Chords.chordAtRoot(tonic, Chords.MAJOR_TRIAD));
		cadence.add(Chords.chordAtRoot(
				tonic.transposeUp(SimpleInterval.PERFECT_FOURTH),
				Chords.MAJOR_TRIAD.invert(2)));
		cadence.add(Chords.chordAtRoot(
				tonic.transposeUp(SimpleInterval.PERFECT_FIFTH),
				Chords.MAJOR_TRIAD.invert(1)));
		cadence.add(Chords.chordAtRoot(tonic, Chords.MAJOR_TRIAD));
		return cadence;
	}

	/**
	 * Returns the tempo in which MIDI sequences should be played,
	 * in beats per minute.
	 *
	 * @return tempo in BPM
	 */
	private double tempo() {
		return 120;
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

	private static class Rest implements PitchGroup {

		@Override
		public Iterator<Pitch> iterator() {
			return Collections.emptyIterator();
		}

		@Override
		public List<Pitch> pitches() {
			return Collections.emptyList();
		}

		@Override
		public int size() {
			return 0;
		}

	}
}
