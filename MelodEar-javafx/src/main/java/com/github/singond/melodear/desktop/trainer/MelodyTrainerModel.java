package com.github.singond.melodear.desktop.trainer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.KeyedMelodyExercise;
import com.github.singond.melodear.KeyedMelodyExerciseFactory;
import com.github.singond.melodear.MelodyExercise;
import com.github.singond.melodear.MelodyTrainer;
import com.github.singond.melodear.desktop.PaneScoped;
import com.github.singond.music.Degree;
import com.github.singond.music.Key;
import com.github.singond.music.Keys;
import com.github.singond.music.Pitch;

@PaneScoped
public class MelodyTrainerModel {

	private static final Set<Degree> DIATONIC_DEGREES;
	private static final Set<Degree> CHROMATIC_DEGREES;
	static {
		DIATONIC_DEGREES = new HashSet<>();
		DIATONIC_DEGREES.addAll(Degree.DIATONIC_DEGREES);
		CHROMATIC_DEGREES = new HashSet<>();
		CHROMATIC_DEGREES.addAll(Degree.CHROMATIC_DEGREES_ASC);
	}

	private static Logger logger = LogManager.getLogger(MelodyTrainerModel.class);

	private MelodyTrainer<KeyedMelodyExercise> trainer;
	/** Start new exercise automatically on completion of the previous. */
	private boolean autoNew = true;

	private final ReadOnlyObjectWrapper<Key> melodyKey
			= new ReadOnlyObjectWrapper<>();
	private final ReadOnlyIntegerWrapper melodyLength
			= new ReadOnlyIntegerWrapper();
	private final ReadOnlyIntegerWrapper notesIdentified
			= new ReadOnlyIntegerWrapper();
	private final ReadOnlyObjectWrapper<MelodyExercise.Status> status
			= new ReadOnlyObjectWrapper<>();
	private ReadOnlyBooleanWrapper running
			= new ReadOnlyBooleanWrapper(false);

	@Inject
	public MelodyTrainerModel() {
		trainer = new MelodyTrainer<>();
		KeyedMelodyExerciseFactory factory = new KeyedMelodyExerciseFactory();
		factory.setKeysAvailable(Arrays.asList(Keys.C_MAJOR, Keys.G_MAJOR, Keys.D_MAJOR));
		factory.setDegreesAvailable(Keys.MAJOR, DIATONIC_DEGREES);
		factory.setDegreesAvailable(Keys.MINOR, DIATONIC_DEGREES);
		factory.setLowerBound(Pitch.C3);
		factory.setUpperBound(Pitch.C6);
		factory.setLength(3);
		trainer.setExerciseFactory(factory);
	}

	public List<Pitch> getMelody() {
		if (trainer.hasExercise()) {
			return trainer.getExercise().melody();
		} else {
			return Collections.emptyList();
		}
	}

	public Key getMelodyKey() {
		return melodyKey.get();
	}

	public ReadOnlyObjectProperty<Key> melodyKeyProperty() {
		return melodyKey.getReadOnlyProperty();
	}

	public int getMelodyLength() {
		return melodyLength.get();
	}

	public ReadOnlyIntegerProperty melodyLengthProperty() {
		return melodyLength.getReadOnlyProperty();
	}

	public int getNotesIdentified() {
		return notesIdentified.get();
	}

	public ReadOnlyIntegerProperty notesIdentifiedProperty() {
		return notesIdentified.getReadOnlyProperty();
	}

	public MelodyExercise.Status getStatus() {
		return status.get();
	}

	public ReadOnlyObjectProperty<MelodyExercise.Status> statusProperty() {
		return status.getReadOnlyProperty();
	}

	public boolean isRunning() {
		return running.get();
	}

	public ReadOnlyBooleanProperty runningProperty() {
		return running.getReadOnlyProperty();
	}

	public void start() {
		logger.debug("Starting trainer");
		running.set(true);
		newExercise();
	}

	public void stop() {
		logger.debug("Stopping trainer");
		running.set(false);
		melodyKey.setValue(null);
		melodyLength.setValue(0);
		notesIdentified.setValue(0);
		status.setValue(null);
	}

	public void newExercise() {
		logger.debug("Starting new exercise");
		KeyedMelodyExercise exercise = trainer.newExercise();
		melodyKey.setValue(exercise.key());
		melodyLength.setValue(exercise.melody().size());
		notesIdentified.setValue(exercise.notesIdentified());
		status.setValue(exercise.status());
	}

	public void evaluate(Pitch pitch) {
		if (trainer.hasExercise() && running.get()) {
			KeyedMelodyExercise exercise = trainer.getExercise();
			MelodyExercise.Status st = exercise.evaluate(pitch);
			status.setValue(st);
			notesIdentified.setValue(exercise.notesIdentified());
			if (logger.isDebugEnabled()) {
				switch (st) {
					case NOT_EVALUATED:
						break;
					case NOTE_INCORRECT:
						logger.debug("Note {} is not correct. Exercise restarted.",
								pitch);
						break;
					case NOTE_CORRECT:
						logger.debug("Note {} is correct.", pitch);
						break;
					case COMPLETED:
						logger.debug("Note {} is correct. Exercise complete.",
								pitch);
						if (autoNew) {
							newExercise();
						}
						break;
					case RESTARTED:
						logger.debug("Exercise restarted.");
						break;
					default:
						break;
				}
			}
		} else {
			// No exercise has been set
		}
	}
}
