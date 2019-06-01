package com.github.singond.melodear.desktop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;
import com.github.singond.music.Pitches;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;

public class Keyboard extends Region {

	private static final Set<PitchClass> PITCH_CLASSES;
	static {
		PITCH_CLASSES = new TreeSet<PitchClass>();
		PITCH_CLASSES.add(PitchClass.C);
		PITCH_CLASSES.add(PitchClass.C_SHARP);
		PITCH_CLASSES.add(PitchClass.D);
		PITCH_CLASSES.add(PitchClass.D_SHARP);
		PITCH_CLASSES.add(PitchClass.E);
		PITCH_CLASSES.add(PitchClass.F);
		PITCH_CLASSES.add(PitchClass.F_SHARP);
		PITCH_CLASSES.add(PitchClass.G);
		PITCH_CLASSES.add(PitchClass.G_SHARP);
		PITCH_CLASSES.add(PitchClass.A);
		PITCH_CLASSES.add(PitchClass.A_SHARP);
		PITCH_CLASSES.add(PitchClass.B);
	}

	private static final Map<PitchClass, KeyDef> KEY_DEFS;
	static {
		KEY_DEFS = new HashMap<>();
		KeyDef def;

		def = new KeyDef(0,   KeyType.WHITE);
		KEY_DEFS.put(PitchClass.C, def);

		def = new KeyDef(15,  KeyType.BLACK);
		KEY_DEFS.put(PitchClass.C_SHARP, def);

		def = new KeyDef(24,  KeyType.WHITE);
		KEY_DEFS.put(PitchClass.D, def);

		def = new KeyDef(43,  KeyType.BLACK);
		KEY_DEFS.put(PitchClass.D_SHARP, def);

		def = new KeyDef(48,  KeyType.WHITE);
		KEY_DEFS.put(PitchClass.E, def);

		def = new KeyDef(72,  KeyType.WHITE);
		KEY_DEFS.put(PitchClass.F, def);

		def = new KeyDef(85,  KeyType.BLACK);
		KEY_DEFS.put(PitchClass.F_SHARP, def);

		def = new KeyDef(96,  KeyType.WHITE);
		KEY_DEFS.put(PitchClass.G, def);

		def = new KeyDef(113, KeyType.BLACK);
		KEY_DEFS.put(PitchClass.G_SHARP, def);

		def = new KeyDef(120, KeyType.WHITE);
		KEY_DEFS.put(PitchClass.A, def);

		def = new KeyDef(141, KeyType.BLACK);
		KEY_DEFS.put(PitchClass.A_SHARP, def);

		def = new KeyDef(144, KeyType.WHITE);
		KEY_DEFS.put(PitchClass.B, def);
	}

	private static final int WHITE_WIDTH = 24;
	private static final int BLACK_WIDTH = 14;
	private static final int WHITE_HEIGHT = 155;
	private static final int BLACK_HEIGHT = 103;
	private static final int OCTAVE_WIDTH = 168;

	private static Logger logger = LogManager.getLogger(Keyboard.class);

	private Pitch lowestPitch;
	private Pitch highestPitch;
	private Key lowestKey;
	private Key highestKey;
	private double leftEdge;

	private KeyboardListener listener = NullListener.INSTANCE;

	public Keyboard() {
		// TODO Enable setting custom range
		construct(Pitch.G4, Pitch.DS6);
		getStyleClass().add("piano-keyboard");
		// A dummy listener for testing
		setListener(new KeyboardListener() {

			@Override
			public void keyDown(Pitch pitch) {
				logger.debug("Pressed {}", pitch);
			}

			@Override
			public void keyUp(Pitch pitch) {}
		});
	}

	private void construct(Pitch low, Pitch high) {
		this.lowestPitch = low;
		this.highestPitch = high;
		KeyDef lowDef = KEY_DEFS.get(low.pitchClass());
		this.leftEdge = lowDef.offset + low.octave() * OCTAVE_WIDTH;
		List<Key> whites = new ArrayList<>();
		List<Key> blacks = new ArrayList<>();
		Key lowKey = null, highKey = null;
		for(Pitch p : Pitches.allBetween(
				lowestPitch, highestPitch, PITCH_CLASSES)) {
			KeyDef def = KEY_DEFS.get(p.pitchClass());
			Key key = new Key(p);
			switch (def.type) {
				case WHITE:
					whites.add(key);
					break;
				case BLACK:
					blacks.add(key);
					break;
				default:
					throw new AssertionError(def.type);
			}
			if (lowKey == null) {
				lowKey = key;
			}
			if (highKey == null || highKey.pitch.compareTo(key.pitch) < 0) {
				highKey = key;
			}
		}
		ObservableList<Node> children = getChildren();
		children.clear();
		children.addAll(whites);
		children.addAll(blacks);
		lowestKey = lowKey;
		highestKey = highKey;
	}

	private double scale(double dimension) {
		return 3*dimension;
	}

	private int scale(int dimension) {
		return 3*dimension;
	}

	private double offset(double position) {
		return position - leftEdge;
	}

	@Override
	protected double computeMinWidth(double h) {
		double w = totalKeysWidth();
		logger.debug("Min width = {}", w);
		return w;
	}

	@Override
	protected double computePrefWidth(double h) {
		double w = totalKeysWidth();
		logger.debug("Pref width = {}", w);
		return w;
	}

	private double totalKeysWidth() {
		if (lowestKey == null || highestKey == null) {
			return 0;
		} else {
			double left = lowestKey.leftExtent;
			double right = highestKey.rightExtent;
			return scale(right - left);
		}
	}

	@Override
	public String getUserAgentStylesheet() {
		return Keyboard.class.getResource("keyboard.css").toExternalForm();
	}

	private class Key extends Button {
		private final Pitch pitch;
		private double leftExtent;
		private double rightExtent;

		Key(Pitch p) {
			this.pitch = p;
			KeyDef keydef = KEY_DEFS.get(p.pitchClass());
			if (keydef == null) {
				throw new AssertionError("Bad pitch for piano key: " + p);
			}
			leftExtent = p.octave() * OCTAVE_WIDTH + keydef.offset;
			rightExtent = leftExtent + keydef.type.width;
			setPrefWidth(scale(keydef.type.width));
			setPrefHeight(scale(keydef.type.height));
			relocate(scale(offset(leftExtent)), 0);
			setTooltip(new Tooltip(p.toString()));
			setOnMousePressed((e) -> listener.keyDown(pitch));
			setOnMouseReleased((e) -> listener.keyUp(pitch));
			getStyleClass().add("piano-key");
			getStyleClass().add("piano-key-" + keydef.type.name);
		}

		@Override
		public String toString() {
			return pitch.toString();
		}
	}

	private static class KeyDef {
		final double offset;
		final KeyType type;

		KeyDef(double offset, KeyType type) {
			this.offset = offset;
			this.type = type;
		}
	}

	private enum KeyType {
		WHITE ("white", WHITE_WIDTH, WHITE_HEIGHT),
		BLACK ("black", BLACK_WIDTH, BLACK_HEIGHT);

		final double width;
		final double height;
		final String name;

		private KeyType(String name, double width, double height) {
			this.name = name;
			this.width = width;
			this.height = height;
		}
	}

	public final void setListener(KeyboardListener listener) {
		if (listener == null) {
			throw new NullPointerException("A listener cannot be null");
		}
		this.listener = listener;
	}

	public final void removeListener() {
		this.listener = NullListener.INSTANCE;
	}

	/**
	 * A default listener with no-op actions.
	 */
	private static class NullListener implements KeyboardListener {

		/** The sole instance. */
		private static final NullListener INSTANCE = new NullListener();

		@Override
		public void keyDown(Pitch pitch) {}

		@Override
		public void keyUp(Pitch pitch) {}

	}
}
