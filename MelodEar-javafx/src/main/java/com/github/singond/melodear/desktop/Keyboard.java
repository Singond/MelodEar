package com.github.singond.melodear.desktop;

import java.util.HashSet;
import java.util.Set;

import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;

import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Keyboard extends Region {

	private static final Set<PitchClass> WHITE_KEYS;
	static {
		WHITE_KEYS = new HashSet<PitchClass>();
		WHITE_KEYS.add(PitchClass.C);
		WHITE_KEYS.add(PitchClass.D);
		WHITE_KEYS.add(PitchClass.E);
		WHITE_KEYS.add(PitchClass.F);
		WHITE_KEYS.add(PitchClass.G);
		WHITE_KEYS.add(PitchClass.A);
		WHITE_KEYS.add(PitchClass.B);
	}

	private static final int WHITE_WIDTH = 24;
	private static final int BLACK_WIDTH = 14;
	private static final int WHITE_HEIGHT = 155;
	private static final int BLACK_HEIGHT = 103;
	private static final int OCTAVE_WIDTH = 168;

	public Keyboard() {
		Key key = new Key(Pitch.C4);
		getChildren().add(key);
	}

	private static boolean isWhiteKey(PitchClass p) {
		return WHITE_KEYS.contains(p);
	}

	private int scale(int dimension) {
		return dimension;
	}

	private class Key extends Control {
//		private final KeySkin skin;

		Key(Pitch p) {
			if (isWhiteKey(p.pitchClass())) {
				this.setSkin(new WhiteKeySkin(this));
			} else {
				// TODO Finish
				throw new UnsupportedOperationException("Not implemented yet");
			}
		}

	}

	private interface KeySkin extends Skin<Key> {

	}

	private class WhiteKeySkin implements KeySkin {

		private final Key control;
//		private Node root;

		public WhiteKeySkin(Key control) {
			this.control = control;
		}

		@Override
		public Key getSkinnable() {
			return control;
		}

		@Override
		public Node getNode() {
			return draw();
		}

		private Node draw() {
			Rectangle rect = new Rectangle();
			rect.setWidth(scale(WHITE_WIDTH));
			rect.setHeight(scale(WHITE_HEIGHT));
			rect.setFill(Color.WHITE);
			return rect;
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

	}
}
