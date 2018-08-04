package com.github.singond.melodear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 *
 * @author Singon
 */
public class KeyedMelodyExcersizeFactory {

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

	/**
	 * Lower bound on the notes in the melody.
	 */
	private Pitch lowerBound;

	/**
	 * Upper bound on the notes in the melody.
	 */
	private Pitch upperBound;

	private transient List<Pitch> pitchesAvailable;

	/**
	 * Length of the melody.
	 */
	private int length;

	private Randomizer rnd;

	public KeyedMelodyExcersizeFactory() {
		degreesAvailable = new HashMap<>();
		rnd = new Randomizer();
		Set<Degree> degrees = new HashSet<>();
		List<Degree> degsList = Arrays.asList(Degree.I, Degree.II,
				Degree.III, Degree.IV, Degree.V, Degree.VI, Degree.VII);
		degrees.addAll(degsList);
		degreesAvailable.put(Keys.MAJOR, degrees);
		degrees = new HashSet<>();
		degrees.addAll(degsList);
		degreesAvailable.put(Keys.MINOR, new HashSet<>(degrees));
	}

	public void setKeysAvailable(List<Key> keysAvailable) {
		this.keysAvailable = keysAvailable;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public void setDegreesAvailable(KeyType keyType, Set<Degree> degrees) {
		this.degreesAvailable.put(keyType, degrees);
	}

	public void setLowerBound(Pitch lowerBound) {
		this.lowerBound = lowerBound;
	}

	public void setUpperBound(Pitch upperBound) {
		this.upperBound = upperBound;
	}

	public void setLength(int length) {
		this.length = length;
	}

	private void randomizeKey() {
		key = rnd.randomFrom(keysAvailable);
	}

	private void updatePitchClasses() {
		if (key == null) {
			throw new IllegalStateException("No key is set");
		}
		if (degreesAvailable == null || degreesAvailable.isEmpty()) {
			throw new IllegalStateException("No available degrees are set");
		}
		List<Degree> degrees = new ArrayList<>(degreesAvailable.get(key.type()));
		Set<PitchClass> pitchClasses = new HashSet<>();
		for (Degree degree : degrees) {
			pitchClasses.add(key.degree(degree));
		}
		pitchClassesAvailable = pitchClasses;
	}

	private void updatePitches() {
		if (pitchClassesAvailable == null
		    || pitchClassesAvailable.isEmpty()) {
			throw new IllegalStateException("No pitch classes available");
		}
		if (lowerBound == null) {
			throw new IllegalStateException("No lower bound set on pitches");
		}
		if (upperBound == null) {
			throw new IllegalStateException("No upper bound set on pitches");
		}
		pitchesAvailable = Pitches.allBetween(lowerBound, upperBound,
				pitchClassesAvailable);
	}

	private List<Pitch> newMelody() {
		List<Pitch> melody = new ArrayList<>(length);
		for (int i = 0; i < length; i++) {
			melody.add(rnd.randomFrom(pitchesAvailable));
		}
		return melody;
	}

//	public KeyedMelodyExcersize make() {
//		List<Degree> degs = degreesAvailable
//	}

	public KeyedMelodyExcersize allNew() {
		randomizeKey();
		updatePitchClasses();
		updatePitches();
		return new KeyedMelodyExcersize(newMelody(), key);
	}
}
