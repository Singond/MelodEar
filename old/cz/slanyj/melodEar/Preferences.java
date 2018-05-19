package cz.slanyj.melodEar;

import java.lang.reflect.Array;
import java.util.EnumSet;
import java.util.HashSet;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSpinner;

import cz.slanyj.music.Key;
import cz.slanyj.music.Key.Mode;

public class Preferences implements ChangeListener {
	
	/* FIELDS */
	// Trainer properties
//	boolean constantKey = true;		// Retain key between excersizes
	/** Number of excersizes per key change. */
	int tasksPerKey = 1;
	// Melody properties
	/** Number of notes in the melody. */
	int melodyLength = 1;
	/**
	 * A set of numbers identifying the span of the melody.<p>
	 * <b>[0]:</b> Key step to start at (tonic = 1)<br>
	 * <b>[1]:</b> Key step to finish at (tonic = 1);<br>
	 * <b>[2]:</b> The octave of the low bound;<br>
	 * <b>[3]:</b> How many times the tonic is crossed when going from the bottom of the span up.
	 * Positive when going up, negative when going down.
	 */
	int[] melodySpan = {1, 1, 4, 1};	// Used in the same way as in Key.scale(), qv.
	
	// The default delays are specified here so they can be changed in a single place.
	// They are static to avoid the need to instantiate Preferences first,
	// if they are used elsewhere, as in the GUI Spinners.
	/** A default value of the delay before key playback. */
	static long keyDelayDft = 1500;
	/** A default value of the delay before the playback of a new task in a new key. */
	static long taskDelayDft = 500;
	/** A default value of the delay before the playback of a new task in the same key. */
	static long newTaskDelayDft = 1500;
	/** Delay before key playback. */
	long keyDelay = keyDelayDft;
	/** Delay before the playback of a new task in a new key. */
	long taskDelay = taskDelayDft;
	/** Delay before the playback of a new task in the same key. */
	long newTaskDelay = newTaskDelayDft;

	private final HashSet<Key> ALL_KEYS = Key.all;
	private HashSet<Key> allowedKeys = ALL_KEYS;
	
	
	/* METHODS */
	Key[] keysAllowed() {
		return allowedKeys.toArray(new Key[]{});
	}
	
	public void selectKeyMode(Mode mode) {
		allowedKeys.clear();
		allowedKeys.addAll(ALL_KEYS);
		for (Key key : allowedKeys.toArray(new Key[]{})) {
			if (key.mode != mode) {allowedKeys.remove(key);}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Something happened!");
//		int val = (int)((JSpinner)e.getSource()).getValue();
//		System.out.println(val);
	}
}
