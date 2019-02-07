package com.github.singond.melodear.desktop;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Keyboard extends Region {

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

	public Keyboard() {
		ObservableList<Node> children = getChildren();
		children.add(new Key(Pitch.C0));
//		children.add(new Key(Pitch.D0));
//		children.add(new Key(Pitch.E0));
//		children.add(new Key(Pitch.F0));
//		children.add(new Key(Pitch.G0));
//		children.add(new Key(Pitch.A0));
//		children.add(new Key(Pitch.B0));
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

	private class Key extends Control {
		private final Pitch pitch;
//		private final KeySkin skin;

		Key(Pitch p) {
			this.pitch = p;
			KeyDef keydef = KEY_DEFS.get(p.pitchClass());
			if (keydef == null) {
				throw new AssertionError("Bad pitch for piano key: " + p);
			}
			this.setSkin(keydef.skin().apply(Keyboard.this, this));
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
