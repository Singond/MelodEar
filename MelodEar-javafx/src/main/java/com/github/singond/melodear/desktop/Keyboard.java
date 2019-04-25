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
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

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

	public Keyboard() {
		// TODO Enable setting custom range
		construct(Pitch.G4, Pitch.E6);
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

	private class Key extends Control {
		private final Pitch pitch;
		private final KeySkin skin;
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
			this.skin = keydef.newSkin(Keyboard.this, this);
			this.relocate(scale(offset(leftExtent)), 0);
			this.setSkin(skin);
			this.setTooltip(new Tooltip(p.toString()));
		}

		@Override
		public String toString() {
			return pitch.toString();
		}
	}

	private interface KeySkin extends Skin<Key> {
	}

	private static abstract class AbstractKeySkin implements KeySkin {
		protected final Key control;
		protected Node root;

		protected static final Paint BORDER_COLOR = Color.gray(0.6);

		public AbstractKeySkin(Key control) {
			this.control = control;
		}

		@Override
		public Key getSkinnable() {
			return control;
		}

		@Override
		public Node getNode() {
//			if (root == null)
			return root = draw();
//			return root;
		}

		protected abstract Node draw();

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

	}

	private class WhiteKeySkin extends AbstractKeySkin implements KeySkin {

		public WhiteKeySkin(Key control) {
			super(control);
		}

		@Override
		protected final Node draw() {
//			logger.debug("Creating skin for key {}", control);
			Rectangle rect = new Rectangle();
			rect.setWidth(scale(WHITE_WIDTH));
			rect.setHeight(scale(WHITE_HEIGHT));
			rect.setFill(Color.WHITE);
			rect.setStroke(BORDER_COLOR);
			rect.setStrokeType(StrokeType.INSIDE);
			rect.setOnMousePressed((e) -> logger.debug("Clicked {}", control));
			return rect;
		}
	}

	private class BlackKeySkin extends AbstractKeySkin implements KeySkin {

		public BlackKeySkin(Key control) {
			super(control);
		}

		@Override
		protected final Node draw() {
//			logger.debug("Creating skin for key {}", control);
			Rectangle rect = new Rectangle();
			rect.setWidth(scale(BLACK_WIDTH));
			rect.setHeight(scale(BLACK_HEIGHT));
			rect.setFill(Color.BLACK);
			rect.setStroke(BORDER_COLOR);
			rect.setStrokeType(StrokeType.INSIDE);
			rect.setOnMousePressed((e) -> logger.debug("Clicked {}", control));
			return rect;
		}
	}

	private static class KeyDef {
		final double offset;
		final KeyType type;

		KeyDef(double offset, KeyType type) {
			this.offset = offset;
			this.type = type;
		}

		KeySkin newSkin(Keyboard kbd, Key control) {
			return type.newSkin(kbd, control);
		}
	}

	private enum KeyType {
		WHITE (WHITE_WIDTH) {
			@Override
			KeySkin newSkin(Keyboard kbd, Key control) {
				return kbd.new WhiteKeySkin(control);
			}
		},
		BLACK (BLACK_WIDTH) {
			@Override
			KeySkin newSkin(Keyboard kbd, Key control) {
				return kbd.new BlackKeySkin(control);
			}
		};

		final double width;

		private KeyType(double width) {
			this.width = width;
		}

		abstract KeySkin newSkin(Keyboard kbd, Key control);
	}
}
