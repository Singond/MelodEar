package com.github.singond.melodear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.music.Degree;
import com.github.singond.music.Key;
import com.github.singond.music.KeyType;
import com.github.singond.music.Keys;
import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;
import com.github.singond.music.Pitches;

/**
 * An implementation of {@link ExerciseFactory} which creates exercises
 * of type {@link KeyedMelodyExercise}.
 * <p>
 * This class is not thread-safe.
 *
 * @author Singon
 */
public final class KeyedMelodyExerciseFactory
		implements ExerciseFactory<KeyedMelodyExercise> {

	private static Logger logger
			= LogManager.getLogger(KeyedMelodyExerciseFactory.class);

	/**
	 * Musical keys available for the musical exercise.
	 * These are stored as list to facilitate random element selection.
	 */
	private List<Key> keysAvailable;

	/**
	 * The current key used to generate exercises.
	 */
	private Key key;
	private transient boolean keyValid;

	/**
	 * The degrees allowed for a given key type.
	 */
	private final Map<KeyType, Set<Degree>> degreesAvailable;

	private transient Set<PitchClass> pitchClassesAvailable;
	private transient boolean pitchClassesAvailableValid;

	/**
	 * Lower bound on the notes in the melody.
	 * The default is the lowest key on the 88-key piano.
	 */
	private Pitch lowerBound = Pitch.A0;

	/**
	 * Upper bound on the notes in the melody.
	 * The default is the highest key on the 88-key piano.
	 */
	private Pitch upperBound = Pitch.C8;

	private transient List<Pitch> pitchesAvailable;
	private transient boolean pitchesAvailableValid;

	/**
	 * Length of the melody.
	 * The default value is 1.
	 */
	private int length = 1;

	private Policy policy = new NoRepeatPolicy();

	/**
	 * Random element generator.
	 * Ensure that this is not null, so that null-checks can be avoided.
	 */
	private Randomizer rnd;

	public KeyedMelodyExerciseFactory() {
		rnd = new Randomizer();

		// Initialize the set of available degrees to the basic degrees
		// of a given key.
		List<Degree> basicDegs = Arrays.asList(Degree.I, Degree.II,
				Degree.III, Degree.IV, Degree.V, Degree.VI, Degree.VII);
		degreesAvailable = new HashMap<>();
		degreesAvailable.put(Keys.MAJOR, new HashSet<>());
		degreesAvailable.put(Keys.MINOR, new HashSet<>());
		degreesAvailable.get(Keys.MAJOR).addAll(basicDegs);
		degreesAvailable.get(Keys.MINOR).addAll(basicDegs);
		pitchClassesAvailableValid = false;
	}

	/**
	 * Invalidates key and all dependent fields.
	 */
	private void invalidateKey() {
		keyValid = false;
		invalidatePitchClassesAvailable();
	}

	/**
	 * Invalidates pitch classes cache and all dependent fields.
	 */
	private void invalidatePitchClassesAvailable() {
		pitchClassesAvailableValid = false;
		invalidatePitchesAvailable();
	}

	/**
	 * Invalidates pitches cache and all dependent fields.
	 */
	private void invalidatePitchesAvailable() {
		pitchesAvailableValid = false;
	}

	public void setKeysAvailable(List<Key> keysAvailable) {
		this.keysAvailable = keysAvailable;
	}

	public void setDegreesAvailable(KeyType keyType, Set<Degree> degrees) {
		this.degreesAvailable.put(keyType, degrees);
		// If the degrees for the current key changed, update the pitch
		// classes cache now, otherwise leave it for the next key change
		if (key != null && key.type().equals(keyType)) {
			invalidatePitchClassesAvailable();
		}
	}

	public void setLowerBound(Pitch lowerBound) {
		this.lowerBound = lowerBound;
		invalidatePitchesAvailable();
	}

	public void setUpperBound(Pitch upperBound) {
		this.upperBound = upperBound;
		invalidatePitchesAvailable();
	}

	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Sets the musical key of generated exercises to the given value.
	 *
	 * @param key the musical key to be used for generated exercises
	 */
	public void setKey(Key key) {
		this.key = key;
		invalidatePitchClassesAvailable();
	}

	/**
	 * Sets the musical key of future geneŕated exercises to a random key
	 * chosen from the collection of available keys.
	 */
	public void newKey() {
		if (keysAvailable == null || keysAvailable.isEmpty())
			throw new IllegalStateException("No keys available");

		Key newKey = rnd.randomFrom(keysAvailable);
		setKey(newKey);
		if (logger.isDebugEnabled())
			logger.debug("Changing key to {}", newKey);
		policy.reset();
		keyValid = true;
	}

	public void setKeyRepeat(int times) {
		if (times < 1) {
			throw new IllegalArgumentException(
					"Number of repeats must be positive");
		}
		if (times == 1) {
			policy = new NoRepeatPolicy();
		} else {
			policy = new RepeatKeyPolicy(times);
		}
	}

	private void updatePitchClasses() {
		// Check prerequisities
		if (!keyValid) newKey();
		if (degreesAvailable == null || degreesAvailable.isEmpty())
			throw new IllegalStateException("No available degrees are set");

		if (logger.isDebugEnabled())
			logger.debug("Regenerating cache of available pitch classes...");

		// Update pitch classes
		Set<PitchClass> pitchClasses = new TreeSet<>();
		for (Degree degree : degreesAvailable.get(key.type())) {
			pitchClasses.add(key.degree(degree));
		}
		pitchClassesAvailable = pitchClasses;
		pitchClassesAvailableValid = true;

		if (logger.isDebugEnabled())
			logger.debug("Pitch classes available: {}", pitchClasses);
	}

	private void updatePitches() {
		// Check prerequisities
		if (!pitchClassesAvailableValid)
			updatePitchClasses();
		if (pitchClassesAvailable == null
		    || pitchClassesAvailable.isEmpty())
			throw new IllegalStateException("No pitch classes available");
		if (lowerBound == null)
			throw new IllegalStateException("No lower bound set on pitches");
		if (upperBound == null)
			throw new IllegalStateException("No upper bound set on pitches");

		if (logger.isDebugEnabled())
			logger.debug("Regenerating cache of available pitches...");

		// Update pitches
		pitchesAvailable = Pitches.allBetween(lowerBound, upperBound,
				pitchClassesAvailable);
		pitchesAvailableValid = true;

		if (logger.isDebugEnabled())
			logger.debug("Pitches available: {}", pitchesAvailable);
	}

	private List<Pitch> newMelody() {
		if (!pitchesAvailableValid)
			updatePitches();

		if (pitchesAvailable == null || pitchesAvailable.isEmpty())
			throw new IllegalStateException("No pitches available");

		List<Pitch> melody = new ArrayList<>(length);
		for (int i = 0; i < length; i++) {
			melody.add(rnd.randomFrom(pitchesAvailable));
		}
		return melody;
	}

	@Override
	public KeyedMelodyExercise make() {
		KeyedMelodyExercise exc = new KeyedMelodyExercise(newMelody(), key);
		policy.exerciseUsed();
		return exc;
	}

	private interface Policy {
		public void exerciseUsed();
		public void reset();
	}

	private class NoRepeatPolicy implements Policy {
		@Override
		public void exerciseUsed() {
			invalidateKey();
		}

		@Override
		public void reset() {
			// Do nothing
		}
	}

	private class RepeatKeyPolicy implements Policy {

		/**
		 * How many times a musical key should be used for subsequent
		 * exercises.
		 */
		private final int repeatKeyTimes;

		/**
		 * How many times the current musical has been used for exercise
		 * since the last key change.
		 */
		private int usedKeyTimes = 0;

		public RepeatKeyPolicy(int repeatKeyTimes) {
			if (repeatKeyTimes < 1) {
				throw new IllegalArgumentException(
						"Number of repeats must be positive");
			}
			this.repeatKeyTimes = repeatKeyTimes;
		}

		@Override
		public void exerciseUsed() {
			if (++usedKeyTimes == repeatKeyTimes) {
				invalidateKey();
			}
		}

		@Override
		public void reset() {
			usedKeyTimes = 0;
		}
	}
}
