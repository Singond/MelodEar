package com.github.singond.melodear.desktop.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;
import com.github.singond.music.Pitches;
import com.github.singond.music.text.PitchFormats;

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

	private ObjectProperty<Pitch> lowestPitch
			= new SimpleObjectProperty<>(Pitch.C4);
	private ObjectProperty<Pitch> highestPitch
			= new SimpleObjectProperty<>(Pitch.C5);

	private Key lowestKey;
	private Key highestKey;
	private double leftEdge;
	private List<Key> keys = new ArrayList<>();

	private KeyboardListener listener = NullListener.INSTANCE;

	private KeyLabelFormat labelFormat = new DefaultKeyLabelFormat();

	public Keyboard() {
		lowestPitch.addListener(o -> {logger.debug("Setting low key");});
		highestPitch.addListener(o -> {logger.debug("Setting high key");});
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

	/**
	 * Returns the value of the {@code from} property.
	 *
	 * @return the value of the property
	 */
	public Pitch getFrom() {
		return lowestPitch.get();
	}

	/**
	 * Sets the value of the {@code from} property.
	 *
	 * @param pitch the new value of the property
	 */
	public void setFrom(Pitch pitch) {
		lowestPitch.set(pitch);
	}

	/**
	 * Returns the {@code from} property.
	 *
	 * @return the property itself
	 */
	public ObjectProperty<Pitch> fromProperty() {
		return lowestPitch;
	}

	/**
	 * Returns the value of the {@code to} property.
	 *
	 * @return the value of the property
	 */
	public Pitch getTo() {
		return highestPitch.get();
	}

	/**
	 * Sets the value of the {@code to} property.
	 *
	 * @param pitch the new value of the property
	 */
	public void setTo(Pitch pitch) {
		highestPitch.set(pitch);
	}

	/**
	 * Returns the {@code to} property.
	 *
	 * @return the property itself
	 */
	public ObjectProperty<Pitch> toProperty() {
		return highestPitch;
	}

	/**
	 * Indicates that the current layout is valid.
	 *
	 * @return {@code true} if the current keys on the keyboard match the
	 *         pitch range given by {@code from} and {@code to} properties
	 */
	private boolean valid() {
		return lowestKey != null && lowestPitch != null
				&& lowestKey.pitch.equals(lowestPitch.get())
				&& highestKey != null && highestPitch != null
				&& highestKey.pitch.equals(highestPitch.get());
	}

	/**
	 * Ensures the child nodes are up to date.
	 */
	private void updateLayout() {
		if (!valid()) {
			layoutKeys(lowestPitch.get(), highestPitch.get());
		}
	}

	private void layoutKeys(Pitch low, Pitch high) {
		if (logger.isDebugEnabled()) {
			logger.debug("Building keyboard from {} to {}", low, high);
		}
		KeyDef lowDef = KEY_DEFS.get(low.pitchClass());
		this.leftEdge = lowDef.offset + low.octave() * OCTAVE_WIDTH;
		List<Key> whites = new ArrayList<>();
		List<Key> blacks = new ArrayList<>();
		Key lowKey = null, highKey = null;
		for(Pitch p : Pitches.allBetween(low, high, PITCH_CLASSES)) {
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
		ObservableList<Node> children = super.getChildren();
		children.clear();
		children.addAll(whites);
		children.addAll(blacks);
		keys.clear();
		keys.addAll(whites);
		keys.addAll(blacks);
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

	@Override
	protected double computeMinHeight(double w) {
		double h = keysHeight();
		logger.debug("Min height = {}", h);
		return h;
	}

	@Override
	protected double computePrefHeight(double w) {
		double h = keysHeight();
		logger.debug("Pref height = {}", h);
		return h;
	}

	/*
	 * XXX: Calling 'new Key(Pitch)' while computing dimensions causes
	 * the height used in VBox layout to be slightly less than the actual
	 * computed height. Have absolutely no idea why.
	 */

	private double totalKeysWidth() {
//		updateLayout();
//		if (lowestKey == null || highestKey == null) {
//			return 0;
//		} else {
//			double left = lowestKey.leftExtent;
//			double right = highestKey.rightExtent;
//			return scale(right - left);
//		}
		Pitch p = lowestPitch.get();
		KeyDef keydef = KEY_DEFS.get(p.pitchClass());
		double leftExtent = p.octave() * OCTAVE_WIDTH + keydef.offset;
		p = highestPitch.get();
		keydef = KEY_DEFS.get(p.pitchClass());
		double rightExtent = p.octave() * OCTAVE_WIDTH + keydef.offset;
		rightExtent += keydef.type.width;
		return scale(rightExtent - leftExtent);
	}

	private double keysHeight() {
		return scale(WHITE_HEIGHT);
	}

	@Override
	protected void layoutChildren() {
		updateLayout();
		super.layoutChildren();
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
			if (p == null) {
				throw new NullPointerException("Key pitch must not be null");
			}
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
			setTooltip(makeTooltip(this));
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

	private final Tooltip makeTooltip(Key key) {
		Tooltip tt = new Tooltip(labelFormat.formatLabel(key.pitch));
		tt.getStyleClass().add("piano-key-tooltip");
		return tt;
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

	public final void setLabelFormat(KeyLabelFormat labelFormat) {
		logger.debug("Setting key label format to {}", labelFormat);
		this.labelFormat = labelFormat;
		for (Key k : keys) {
			k.setTooltip(makeTooltip(k));
		}
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

	private static class DefaultKeyLabelFormat implements KeyLabelFormat {

		@Override
		public String formatLabel(Pitch pitch) {
			return PitchFormats.SCIENTIFIC.format(pitch).toString();
		}

	}
}
