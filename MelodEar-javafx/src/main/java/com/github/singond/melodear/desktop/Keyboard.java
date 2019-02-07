package com.github.singond.melodear.desktop;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;

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
		KeyDef c =  new KeyDef((kbd, k) -> kbd.new WhiteKeySkin(k, 0  ));
//		KeyDef cd = new KeyDef((kbd, k) -> kbd.new BlackKeySkin(k, 15 ));
		KeyDef d =  new KeyDef((kbd, k) -> kbd.new WhiteKeySkin(k, 24 ));
//		KeyDef de = new KeyDef((kbd, k) -> kbd.new BlackKeySkin(k, 43 ));
		KeyDef e =  new KeyDef((kbd, k) -> kbd.new WhiteKeySkin(k, 48 ));
		KeyDef f =  new KeyDef((kbd, k) -> kbd.new WhiteKeySkin(k, 72 ));
//		KeyDef fg = new KeyDef((kbd, k) -> kbd.new BlackKeySkin(k, 85 ));
		KeyDef g =  new KeyDef((kbd, k) -> kbd.new WhiteKeySkin(k, 96 ));
//		KeyDef ga = new KeyDef((kbd, k) -> kbd.new BlackKeySkin(k, 113));
		KeyDef a =  new KeyDef((kbd, k) -> kbd.new WhiteKeySkin(k, 120));
//		KeyDef ab = new KeyDef((kbd, k) -> kbd.new BlackKeySkin(k, 141));
		KeyDef b =  new KeyDef((kbd, k) -> kbd.new WhiteKeySkin(k, 144));
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

	public Keyboard() {
		lowestPitch = Pitch.C0;
		highestPitch = Pitch.B0;
		populate();
	}

	private void populate() {
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
		return position;
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
			double left = ((Key)kids.get(0)).leftExtent();
			double right = ((Key)kids.get(kids.size() -1)).rightExtent();
			return scale(right - left);
		}
	}

	private class Key extends Control {
		private final Pitch pitch;
		private final KeySkin skin;

		Key(Pitch p) {
			this.pitch = p;
			KeyDef keydef = KEY_DEFS.get(p.pitchClass());
			if (keydef == null) {
				throw new AssertionError("Bad pitch for piano key: " + p);
			}
			this.skin = keydef.skin().apply(Keyboard.this, this);
			this.setSkin(skin);
		}

		@Override
		public String toString() {
			return pitch.toString();
		}

		public double leftExtent() {
			return skin.leftExtent();
		}

		public double rightExtent() {
			return skin.rightExtent();
		}
	}

	private interface KeySkin extends Skin<Key> {

		public double leftExtent();

		public double rightExtent();
	}

	private class WhiteKeySkin implements KeySkin {

		private final Key control;
		private final double xposition;
		private Node root;

		public WhiteKeySkin(Key control, double xpos) {
			this.control = control;
			this.xposition = xpos;
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

		@Override
		public double leftExtent() {
			return xposition;
		}

		@Override
		public double rightExtent() {
			return xposition + WHITE_WIDTH;
		}

		private Node draw() {
			logger.debug("Creating skin for key {}", control);
			Rectangle rect = new Rectangle();
			rect.setX(scale(offset(xposition)));
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
		private final BiFunction<Keyboard, Key, KeySkin> skin;

		public KeyDef(BiFunction<Keyboard, Key, KeySkin> skin) {
			super();
			this.skin = skin;
		}

		BiFunction<Keyboard, Key, KeySkin> skin() {
			return skin;
		}
	}
}
