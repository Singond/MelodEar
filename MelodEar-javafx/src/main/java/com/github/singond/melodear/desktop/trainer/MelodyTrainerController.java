package com.github.singond.melodear.desktop.trainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.inject.Named;

import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.MelodyExercise.NoteStatus;
import com.github.singond.melodear.desktop.PaneScoped;
import com.github.singond.melodear.desktop.audio.AudioDevice;
import com.github.singond.melodear.desktop.components.Keyboard;
import com.github.singond.melodear.desktop.components.KeyboardListener;
import com.github.singond.melodear.desktop.keyboard.KeyboardSettings;
import com.github.singond.melodear.desktop.settings.AllSettings;
import com.github.singond.music.Chords;
import com.github.singond.music.Key;
import com.github.singond.music.Pitch;
import com.github.singond.music.PitchGroup;
import com.github.singond.music.SimpleInterval;

@PaneScoped
public class MelodyTrainerController {

	private static Logger logger = LogManager.getLogger(MelodyTrainerController.class);

	private static final String NOTE_CORRECT_CLASS = "correct";
	private static final String NOTE_INCORRECT_CLASS = "incorrect";
	private static final String EXERCISE_COMPLETED_CLASS = "completed";

	private final MelodyTrainerModel trainerModel;
	private final KeyboardListener keyboardListener;

	private KeyFormatter keyFormatter;
	private FadeTransition noteStatusFader;
	/** Play exercise automatically when it is created. */
	private boolean autoPlayExercise;

	@Inject
	AudioDevice audio;

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
	public MelodyTrainerController(AllSettings settings,
			MelodyTrainerModel trainerModel,
			MelodyTrainerKeyboardListener keyboardListener) {
		logger.trace("Creating MelodyTrainerController");
		kbdSettings = settings.keyboard();
		keyFormatter = new KeyFormatter();
		this.trainerModel = trainerModel;
		this.keyboardListener = keyboardListener;
	}

	/**
	 * Initializes this controller.
	 * If the controller is reused between views, this method is called
	 * every time a new view is built.
	 */
	public void initialize() {
		logger.debug("Initializing melody trainer controller.");
		keyboard.setLabelFormat(kbdSettings.getKeyLabelFormat());
		keyboard.setListener(keyboardListener);
		kbdSettings.keyLabelFormatProperty().addListener((v, o, n) -> {
			logger.debug("Setting label format");
			keyboard.setLabelFormat(n);
		});
		keyName.textProperty().bind(Bindings.createStringBinding(
				() -> keyFormatter.format(trainerModel.getMelodyKey()),
				trainerModel.melodyKeyProperty()));
		keyNameLabel.setText(bundle.getString("keymel.key") + ": ");
		noteStatusFader = new FadeTransition(Duration.millis(500), noteStatus);
		noteStatusFader.setFromValue(1);
		noteStatusFader.setToValue(0);
		noteStatusFader.setDelay(Duration.millis(1000));
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
		trainerModel.onNewExercise(() -> onNewExercise());
	}

	private String noteStatusClass(NoteStatus status) {
		if (status == null) {
			return "";
		}
		switch (status) {
			case NOTE_CORRECT:
				return NOTE_CORRECT_CLASS;
			case NOTE_INCORRECT:
				return NOTE_INCORRECT_CLASS;
			case COMPLETED:
				return EXERCISE_COMPLETED_CLASS;
			default:
				return "";
		}
	}

	public void noteEvaluated(NoteStatus status) {
		if (status == null) {
			return;
		}
		// Set text
		String str = status.toString().toLowerCase().replace("_", " ");
		str = str.substring(0, 1).toUpperCase() + str.substring(1);
		noteStatus.setText(str);
		// Set style class
		ObservableList<String> classes = noteStatus.getStyleClass();
		classes.clear();
		classes.add(this.noteStatusClass(status));
		// Set opacity and (re)set fade animation
		noteStatus.setOpacity(1);
		noteStatusFader.stop();
		noteStatusFader.playFromStart();
	}

	private void onNewExercise() {
		if (autoPlayExercise) {
			Task<Void> delay = new Task<>() {
				@Override
				protected Void call() throws Exception {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// Do nothing
					}
					return null;
				}
			};
			delay.setOnSucceeded((e) -> playKeyAndMelody());
			new Thread(delay).start();
		}
	}

	public void startStopExercise() {
		logger.debug("'Start/stop exercise' pressed.");
		if (trainerModel.isRunning()) {
			// Stop
			trainerModel.stop();
			autoPlayExercise = false;
		} else {
			// Start
			trainerModel.start();
			playKeyAndMelody();
			autoPlayExercise = true;
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
