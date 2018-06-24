package com.github.singond.android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;

public class PianoKeyboard extends RelativeLayout {

	private static final String TAG = "PianoKeyboard";

	private static final int whiteKeyWidth = 24;
	private static final int blackKeyWidth = 14;
	private static final int whiteKeyHeight = 155;
	private static final int blackKeyHeight = 103;
	private static final int octaveWidth = 168;
	/**
	 * Distances of piano keys from the left edge of the octave.
	 */
	private static final Map<PitchClass, Integer> keyOffsets;
	static {
		keyOffsets = new HashMap<>();
		keyOffsets.put(PitchClass.C,         0);
		keyOffsets.put(PitchClass.C_SHARP,  15);
		keyOffsets.put(PitchClass.D,        24);
		keyOffsets.put(PitchClass.D_SHARP,  43);
		keyOffsets.put(PitchClass.E,        48);
		keyOffsets.put(PitchClass.F,        72);
		keyOffsets.put(PitchClass.F_SHARP,  85);
		keyOffsets.put(PitchClass.G,        96);
		keyOffsets.put(PitchClass.G_SHARP,  113);
		keyOffsets.put(PitchClass.A,        120);
		keyOffsets.put(PitchClass.A_SHARP,  141);
		keyOffsets.put(PitchClass.B,        144);
	}
	private static final Set<PitchClass> whiteKeys, blackKeys;
	static {
		whiteKeys = new HashSet<>();
		whiteKeys.add(PitchClass.C);
		whiteKeys.add(PitchClass.D);
		whiteKeys.add(PitchClass.E);
		whiteKeys.add(PitchClass.F);
		whiteKeys.add(PitchClass.G);
		whiteKeys.add(PitchClass.A);
		whiteKeys.add(PitchClass.B);

		blackKeys = new HashSet<>();
		blackKeys.add(PitchClass.C_SHARP);
		blackKeys.add(PitchClass.D_SHARP);
		blackKeys.add(PitchClass.F_SHARP);
		blackKeys.add(PitchClass.G_SHARP);
		blackKeys.add(PitchClass.A_SHARP);
	}

	/** Rendering scale of the keyboard */
	private int dp;

	/** Note event listener */
	private Listener listener;

	/** The lowest key to be displayed. */
	private Pitch lowestKey;

	/** The highest key to be displayed. */
	private Pitch highestKey;

	private Paint whiteKeyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint blackKeyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint keyBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	{
		whiteKeyPaint.setStyle(Paint.Style.FILL);
		whiteKeyPaint.setColor(0xffffffff);

		blackKeyPaint.setStyle(Paint.Style.FILL);
		blackKeyPaint.setColor(0xff000000);

		keyBorderPaint.setStyle(Paint.Style.STROKE);
		keyBorderPaint.setColor(0xff404040);
	}

	public PianoKeyboard(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Testing only
		List<Pitch> pitches = new ArrayList<>(13);
		pitches.add(Pitch.of(PitchClass.C, 4));
		pitches.add(Pitch.of(PitchClass.C_SHARP, 4));
		pitches.add(Pitch.of(PitchClass.D, 4));
		pitches.add(Pitch.of(PitchClass.D_SHARP, 4));
		pitches.add(Pitch.of(PitchClass.E, 4));
		pitches.add(Pitch.of(PitchClass.F, 4));
		pitches.add(Pitch.of(PitchClass.F_SHARP, 4));
		pitches.add(Pitch.of(PitchClass.G, 4));
		pitches.add(Pitch.of(PitchClass.G_SHARP, 4));
		pitches.add(Pitch.of(PitchClass.A, 4));
		pitches.add(Pitch.of(PitchClass.A_SHARP, 4));
		pitches.add(Pitch.of(PitchClass.B, 4));
		pitches.add(Pitch.of(PitchClass.C, 5));

		buildKeyboard(pitches, 0, context);
//		buildKeyboard(lowestKey, highestKey, 0, context);
	}

	private void buildKeyboard(Pitch from, Pitch to, int leftOffset, Context context) {
		// TODO Generate pitch sequence
		List<Pitch> pitches = null;
		buildKeyboard(pitches, leftOffset, context);
	}

	private void buildKeyboard(List<Pitch> pitches, int leftOffset, Context context) {
		for (Pitch p : pitches) {
			Key key;
			if (whiteKeys.contains(p)) {
				key = new WhiteKey(context, p);
			} else if (blackKeys.contains(p)) {
				key = new BlackKey(context, p);
			} else {
				throw new AssertionError("Black keys are designated by the sharp pitch class");
			}
			key.setOffset(leftOffset);
		}
	}

	private abstract class Key extends View {

		/** The pitch of the key */
		private final Pitch pitch;

		/** Position on the keyboard */
		final int position;

		private LayoutParams layout;

		Key(Context context, Pitch pitch) {
			super(context);
			if (pitch == null) {
				throw new NullPointerException("Pitch of a piano key must not be null");
			}
			this.pitch = pitch;
			this.position = pitch.octave() * octaveWidth
			                + keyOffsets.get(pitch.pitchClass());
			this.layout = new LayoutParams(width(), height());
			setLayoutParams(layout);
			setOnTouchListener(new KeyListener());
		}

		abstract int width();
		abstract int height();

		void setOffset(int offset) {
			layout.leftMargin = position + offset;
		}

		private class KeyListener implements OnTouchListener {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						Log.v(TAG, "Pressed key " + pitch);
						listener.keyPressed(pitch);
						return true;
					case MotionEvent.ACTION_UP:
						Log.v(TAG, "Released key " + pitch);
						listener.keyReleased(pitch);
						return true;
				}
				return false;
			}
		}
	}

	private class WhiteKey extends Key {

		private static final int width = whiteKeyWidth;
		private static final int height = whiteKeyHeight;

		public WhiteKey(Context context, Pitch pitch) {
			super(context, pitch);
		}

		@Override
		int width() {
			return width;
		}

		@Override
		int height() {
			return height;
		}

		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawRect(new Rect(0, 0, width, height), whiteKeyPaint);
			canvas.drawRect(new Rect(0, 0, width, height), keyBorderPaint);
		}
	}

	private class BlackKey extends Key {

		private static final int width = blackKeyWidth;
		private static final int height = blackKeyHeight;

		public BlackKey(Context context, Pitch pitch) {
			super(context, pitch);
			setLayoutParams(new LayoutParams(width, height));
		}

		@Override
		int width() {
			return width;
		}

		@Override
		int height() {
			return height;
		}

		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawRect(new Rect(0, 0, width, height), blackKeyPaint);
			canvas.drawRect(new Rect(0, 0, width, height), keyBorderPaint);
		}
	}

	/**
	 * A listener for events occuring on the keyboard.
	 */
	public interface Listener {

		/**
		 * Invoked when a keyboard key is pressed.
		 */
		public void keyPressed(Pitch pitch);

		/**
		 * Invoked when a keyboard key is released.
		 */
		public void keyReleased(Pitch pitch);
	}
}
