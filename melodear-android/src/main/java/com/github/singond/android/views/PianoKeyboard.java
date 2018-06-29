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

import org.billthefarmer.mididriver.MidiDriver;

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
	private int dp = 4;

	/** Note event listener */
	private Listener listener;

	/** The lowest key to be displayed. */
	private Pitch lowestKey;

	/** The highest key to be displayed. */
	private Pitch highestKey;

	private MidiDriver mainmidi = new MidiDriver();

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

		buildKeyboard(pitches, 672, context);
//		buildKeyboard(lowestKey, highestKey, 0, context);
//		listener = new TestListener(new MidiDriver());
	}

	private void buildKeyboard(Pitch from, Pitch to, int leftOffset, Context context) {
		// TODO Generate pitch sequence
		List<Pitch> pitches = null;
		buildKeyboard(pitches, leftOffset, context);
	}

	private void buildKeyboard(List<Pitch> pitches, int leftOffset, Context context) {
		List<Key> whites = new ArrayList<>();
		List<Key> blacks = new ArrayList<>();
		for (Pitch p : pitches) {
			Key key;
			if (whiteKeys.contains(p.pitchClass())) {
				key = new WhiteKey(context, p);
				whites.add(key);
			} else if (blackKeys.contains(p.pitchClass())) {
				key = new BlackKey(context, p);
				blacks.add(key);
			} else {
				throw new AssertionError("Black keys are designated by the sharp pitch class");
			}
		}
		for (Key k : whites) {
			addView(k);
			k.setOffset(leftOffset);
		}
		for (Key k : blacks) {
			addView(k);
			k.setOffset(leftOffset);
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
			this.position = pitch.octave() * octaveWidth*dp
			                + keyOffsets.get(pitch.pitchClass())*dp;
			this.layout = new LayoutParams(width(), height());
			setLayoutParams(layout);
			setOnTouchListener(new KeyListener());
		}

		abstract int width();
		abstract int height();

		void setOffset(int offset) {
			layout.leftMargin = position - offset*dp;
		}

		private class KeyListener implements OnTouchListener {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						Log.v(TAG, "Pressed key " + pitch);
						byte[] msg;
						msg = new byte[3];
						msg[0] = (byte)(0x90); // note on
						msg[1] = (byte)(pitch.midiNumber());
						msg[2] = (byte)(127); // max velocity
						mainmidi.write(msg);
						if (listener != null) {
							listener.keyPressed(pitch);
						}
						return true;
					case MotionEvent.ACTION_UP:
						Log.v(TAG, "Released key " + pitch);
						msg = new byte[3];
						msg[0] = (byte)(0x80); // note off
						msg[1] = (byte)(pitch.midiNumber());
						msg[2] = (byte)(0); // max velocity
						mainmidi.write(msg);
						if (listener != null) {
							listener.keyReleased(pitch);
						}
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
			return width*dp;
		}

		@Override
		int height() {
			return height*dp;
		}

		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawRect(new Rect(0, 0, width*dp, height*dp), whiteKeyPaint);
			canvas.drawRect(new Rect(0, 0, width*dp, height*dp), keyBorderPaint);
		}
	}

	private class BlackKey extends Key {

		private static final int width = blackKeyWidth;
		private static final int height = blackKeyHeight;

		public BlackKey(Context context, Pitch pitch) {
			super(context, pitch);
//			setLayoutParams(new LayoutParams(width, height));
		}

		@Override
		int width() {
			return width*dp;
		}

		@Override
		int height() {
			return height*dp;
		}

		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawRect(new Rect(0, 0, width*dp, height*dp), blackKeyPaint);
			canvas.drawRect(new Rect(0, 0, width*dp, height*dp), keyBorderPaint);
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


	private static class TestListener implements Listener {

		private MidiDriver midi;

		public TestListener(MidiDriver midi) {
			this.midi = midi;
		}

		@Override
		public void keyPressed(Pitch pitch) {
			Log.v(TAG, "Pressed key of pitch " + pitch);
			byte[] msg;
			msg = new byte[3];
			msg[0] = (byte)(0x90); // note on
			msg[1] = (byte)(pitch.midiNumber());
			msg[2] = (byte)(127); // max velocity
			midi.write(msg);
		}

		@Override
		public void keyReleased(Pitch pitch) {
			byte[] msg;
			Log.v(TAG, "Released key of pitch " + pitch);
			msg = new byte[3];
			msg[0] = (byte)(0x80); // note off
			msg[1] = (byte)(pitch.midiNumber());
			msg[2] = (byte)(0); // max velocity
			midi.write(msg);
		}

	}
}
