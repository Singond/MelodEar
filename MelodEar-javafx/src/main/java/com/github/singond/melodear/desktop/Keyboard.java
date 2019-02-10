package com.github.singond.melodear.desktop;

import java.util.HashMap;
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
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Keyboard extends Region {

	private static final Set<PitchClass> PITCH_CLASSES;
	static {
		PITCH_CLASSES = new TreeSet<PitchClass>();
		PITCH_CLASSES.add(PitchClass.C);
//		PITCH_CLASSES.add(PitchClass.C_SHARP);
		PITCH_CLASSES.add(PitchClass.D);
//		PITCH_CLASSES.add(PitchClass.D_SHARP);
		PITCH_CLASSES.add(PitchClass.E);
		PITCH_CLASSES.add(PitchClass.F);
//		PITCH_CLASSES.add(PitchClass.F_SHARP);
		PITCH_CLASSES.add(PitchClass.G);
//		PITCH_CLASSES.add(PitchClass.G_SHARP);
		PITCH_CLASSES.add(PitchClass.A);
//		PITCH_CLASSES.add(PitchClass.A_SHARP);
		PITCH_CLASSES.add(PitchClass.B);
	}

	private static final Map<PitchClass, KeyDef> KEY_DEFS;
	static {
		KeyDef c =  new KeyDef(0,   KeyType.WHITE);
//		KeyDef cd = new KeyDef(15,  KeyType.BLACK);
		KeyDef d =  new KeyDef(24,  KeyType.WHITE);
//		KeyDef de = new KeyDef(43,  KeyType.BLACK);
		KeyDef e =  new KeyDef(48,  KeyType.WHITE);
		KeyDef g =  new KeyDef(96,  KeyType.WHITE);
		KeyDef f =  new KeyDef(72,  KeyType.WHITE);
//		KeyDef fg = new KeyDef(85,  KeyType.BLACK);
//		KeyDef ga = new KeyDef(113, KeyType.BLACK);
		KeyDef a =  new KeyDef(120, KeyType.WHITE);
//		KeyDef ab = new KeyDef(141, KeyType.BLACK);
		KeyDef b =  new KeyDef(144, KeyType.WHITE);
		KEY_DEFS = new HashMap<>();
		KEY_DEFS.put(PitchClass.C, c);
		KEY_DEFS.put(PitchClass.D, d);
		KEY_DEFS.put(PitchClass.E, e);
		KEY_DEFS.put(PitchClass.F, f);
		KEY_DEFS.put(PitchClass.G, g);
		KEY_DEFS.put(PitchClass.A, a);
		KEY_DEFS.put(PitchClass.B, b);
	}

	private static final int WHITE_WIDTH = 24;
	private static final int BLACK_WIDTH = 14;
	private static final int WHITE_HEIGHT = 155;
	private static final int BLACK_HEIGHT = 103;
	private static final int OCTAVE_WIDTH = 168;

	private static Logger logger = LogManager.getLogger(Keyboard.class);

	private Pitch lowestPitch;
	private Pitch highestPitch;
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
		ObservableList<Node> children = getChildren();
		children.clear();
		for(Pitch p : Pitches.allBetween(
				lowestPitch, highestPitch, PITCH_CLASSES)) {
			children.add(new Key(p));
		}
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
		ObservableList<Node> kids = getChildren();
		if (kids.isEmpty()) {
			return 0;
		} else {
			double left = ((Key)kids.get(0)).leftExtent;
			double right = ((Key)kids.get(kids.size() -1)).rightExtent;
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
			rightExtent = leftExtent + WHITE_WIDTH;
			this.skin = keydef.newSkin(Keyboard.this, this);
			this.relocate(scale(offset(leftExtent)), 0);
			this.setSkin(skin);
		}

		@Override
		public String toString() {
			return pitch.toString();
		}
	}

	private interface KeySkin extends Skin<Key> {
	}

	private class WhiteKeySkin implements KeySkin {

		private final Key control;
		private Node root;

		public WhiteKeySkin(Key control) {
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

		private Node draw() {
			logger.debug("Creating skin for key {}", control);
			Rectangle rect = new Rectangle();
			rect.setWidth(scale(WHITE_WIDTH));
			rect.setHeight(scale(WHITE_HEIGHT));
			rect.setFill(Color.WHITE);
			rect.setStroke(Color.gray(0.3));
			rect.setOnMousePressed((e) -> logger.debug("Clicked {}", control));
			return rect;
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
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
		WHITE {
			@Override
			KeySkin newSkin(Keyboard kbd, Key control) {
				return kbd.new WhiteKeySkin(control);
			}
		},
		BLACK {
			@Override
			KeySkin newSkin(Keyboard kbd, Key control) {
				throw new UnsupportedOperationException("Not implemented yet");
			}
		};

		abstract KeySkin newSkin(Keyboard kbd, Key control);
	}
}
