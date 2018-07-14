package com.github.singond.android.views;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.billthefarmer.mididriver.MidiDriver;

import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class PianoKeyboard extends RelativeLayout {

	private static final String TAG = "PianoKeyboard";

	/* Generic keyboard parametres in relative measure */

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

	private static final Set<PitchClass> whiteKeys, blackKeys, allKeys;
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

		allKeys = new HashSet<>();
		allKeys.addAll(whiteKeys);
		allKeys.addAll(blackKeys);
	}

	private Listener listener;

	/**
	 * Unit of the keyboard's dimensions.
	 * This determines the drawing size of the child views.
	 */
	private int unit = 2 * (int) getResources().getDisplayMetrics().density;

	private final MidiDriver midi;

	public PianoKeyboard(Context context, AttributeSet attrs) {
		super(context, attrs);

		PianoKey pb = new PianoKey(context, Pitch.of(PitchClass.C, 4)); // middle C
		addView(pb);

		pb = new PianoKey(context, Pitch.of(PitchClass.D, 4));
		addView(pb);

		pb = new PianoKey(context, Pitch.of(PitchClass.E, 4));
		addView(pb);

		pb = new PianoKey(context, Pitch.of(PitchClass.F, 4));
		addView(pb);

		pb = new PianoKey(context, Pitch.of(PitchClass.G, 4));
		addView(pb);

		pb = new PianoKey(context, Pitch.of(PitchClass.A, 4));
		addView(pb);

		pb = new PianoKey(context, Pitch.of(PitchClass.B, 4));
		addView(pb);

		pb = new PianoKey(context, Pitch.of(PitchClass.C, 5));
		addView(pb);

		/*
		 * Make sure to create only one MidiDriver and start it only once
		 * since its creation or last call to stop().
		 * Otherwise the MIDI sound will stutter.
		 * (The current setup sounds fine at first, but breaks when the
		 * screen is rotated).
		 */
		midi = new MidiDriver();
		midi.start();
		setListener(new TestListener(midi));
	}

	/**
	 * Assigns the given object to listen for events in this keyboard.
	 *
	 * @param listener the object to listen for keyboard events
	 */
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	private class PianoKey extends View {

		private final Pitch pitch;

		private LayoutParams layout;
		private Paint p;
		private Paint pBorder;
		private Paint pText;

		public PianoKey(Context context, Pitch pitch) {
			super(context);
			this.pitch = pitch;

			layout = new LayoutParams(width()*unit, height()*unit);
			layout.leftMargin = (position() - 4*octaveWidth) * unit;
			setLayoutParams(layout);

			p = new Paint(Paint.ANTI_ALIAS_FLAG);
			p.setColor(0xff7777dd);
			pBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
			pBorder.setStyle(Paint.Style.STROKE);
			pBorder.setColor(0xff404040);
			pText = new Paint(Paint.ANTI_ALIAS_FLAG);
			pText.setColor(0xffffffff);

			setOnTouchListener(new PianoKeyListener());
		}

		/**
		 * Returns the unscaled width of this key.
		 *
		 * @return the width of this key in relative measure
		 */
		int width() {
			return whiteKeyWidth;
		}

		/**
		 * Returns the unscaled height of this key.
		 * <p>
		 * Note that the word <em>height</em> refers to the display here.
		 * In real world, this would correspond to the <em>length</em>
		 * of the key.
		 *
		 * @return the height of this key in relative measure
		 */
		int height() {
			return whiteKeyHeight;
		}

		/**
		 * Returns the position of this key on the keyboard.
		 *
		 * More precisely, this method returns the unscaled horizontal
		 * distance of the left edge of this key from the reference position.
		 * The reference position is the left edge of the key C0
		 * (in scientific notation).
		 *
		 * @param the horizontal position of this key in relative measure
		 */
		int position() {
			return keyOffsets.get(pitch.pitchClass()) + octaveWidth * pitch.octave();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawRect(new Rect(0, 0, width()*unit, height()*unit), p);
			canvas.drawRect(new Rect(0, 0, width()*unit, height()*unit), pBorder);
		}

		private class PianoKeyListener implements OnTouchListener {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
//						Log.d(TAG, "Pressed key " + pitch);
						if (listener != null) {
							listener.keyPressed(pitch);
						}
						return true;
					case MotionEvent.ACTION_UP:
//						Log.d(TAG, "Released key " + pitch);
						if (listener != null) {
							listener.keyReleased(pitch);
						}
						return true;
				}
				return false;
			}
		}
	}

	/**
	 * A listener for events occuring on the keyboard.
	 */
	public interface Listener {

		/**
		 * Invoked when a keyboard key is pressed.
		 *
		 * @param pitch the pitch of the key which was depressed
		 */
		public void keyPressed(Pitch pitch);

		/**
		 * Invoked when a keyboard key is released.
		 *
		 * @param pitch the pitch of the key which was released
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
			Log.v(TAG, "Listener: pressed key " + pitch);
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
			Log.v(TAG, "Listener: released key " + pitch);
			msg = new byte[3];
			msg[0] = (byte)(0x80); // note off
			msg[1] = (byte)(pitch.midiNumber());
			msg[2] = (byte)(0); // max velocity
			midi.write(msg);
		}

	}
}
