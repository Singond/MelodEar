package com.github.singond.melodear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.singond.music.Degree;
import com.github.singond.music.Key;
import com.github.singond.music.KeyType;
import com.github.singond.music.Keys;
import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;
import com.github.singond.music.Pitches;

/**
 * Keeps track of constraints for creating new melodic excersizes in a key
 * and creates new instances of the excersizes based on these constraints.
 * <p>
 * This class is not thread-safe.
 *
 * @author Singon
 */
public class KeyedMelodyExcersizeFactory {

	private static Logger logger = LoggerFactory.getLogger(KeyedMelodyExcersizeFactory.class);

	/**
	 * Musical keys available for the musical excersize.
	 * These are stored as list to facilitate random element selection.
	 */
	private List<Key> keysAvailable;

	/**
	 * The current key used to generate excersizes.
	 */
	private Key key;

	/**
	 * The degrees allowed for a given key type.
	 */
	private final Map<KeyType, Set<Degree>> degreesAvailable;

	private transient Set<PitchClass> pitchClassesAvailable;
	private boolean pitchClassesAvailableValid;

	/**
	 * Lower bound on the notes in the melody.
	 */
	private Pitch lowerBound;

	/**
	 * Upper bound on the notes in the melody.
	 */
	private Pitch upperBound;

	private transient List<Pitch> pitchesAvailable;
	private boolean pitchesAvailableValid;

	/**
	 * Length of the melody.
	 */
	private int length;

	/**
	 * Random element generator.
	 * Ensure that this is not null, so that null-checks can be avoided.
	 */
	private Randomizer rnd;

	public KeyedMelodyExcersizeFactory() {
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

	public void setKeysAvailable(List<Key> keysAvailable) {
		this.keysAvailable = keysAvailable;
	}

	public void setDegreesAvailable(KeyType keyType, Set<Degree> degrees) {
		this.degreesAvailable.put(keyType, degrees);
		// If the degrees for the current key changed, update the pitch
		// classes cache now, otherwise leave it for the next key change
		if (key != null && key.type().equals(keyType)) {
			pitchClassesAvailableValid = false;
		}
	}

	public void setLowerBound(Pitch lowerBound) {
		this.lowerBound = lowerBound;
		pitchesAvailableValid = false;
	}

	public void setUpperBound(Pitch upperBound) {
		this.upperBound = upperBound;
		pitchesAvailableValid = false;
	}

	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Sets the musical key of generated excersizes to the given value.
	 *
	 * @param key the musical key to be used for generated excersizes
	 */
	public void setKey(Key key) {
		this.key = key;
		pitchClassesAvailableValid = false;
		pitchesAvailableValid = false;
	}

	/**
	 * Sets the musical key of future geneŕated excersizes to a random key
	 * chosen from the collection of available keys.
	 */
	public void newKey() {
		if (keysAvailable == null || keysAvailable.isEmpty())
			throw new IllegalStateException("No keys available");

		setKey(rnd.randomFrom(keysAvailable));
		if (logger.isDebugEnabled())
			logger.debug("Setting the key to {}", key);
	}

	private void updatePitchClasses() {
		if (key == null)
			throw new IllegalStateException("No key is set");
		if (degreesAvailable == null || degreesAvailable.isEmpty())
			throw new IllegalStateException("No available degrees are set");

		if (logger.isDebugEnabled())
			logger.debug("Regenerating cache of available pitch classes...");

		// Do the work
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
		// Update prerequisities
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

		// Do the work
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

	/**
	 * Creates a new excersize with a random melody in the current key.
	 *
	 * @return a new melody excersize in the current key
	 */
	public KeyedMelodyExcersize make() {
		return new KeyedMelodyExcersize(newMelody(), key);
	}

//	public KeyedMelodyExcersize allNew() {
//		newKey();
//		updatePitchClasses();
//		updatePitches();
//		return new KeyedMelodyExcersize(newMelody(), key);
//	}
}
