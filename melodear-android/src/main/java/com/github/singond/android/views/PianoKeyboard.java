package com.github.singond.android.views;

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

	private Listener listener;

	@Deprecated
	private final int dp = (int) getResources().getDisplayMetrics().density;
	/**
	 * Unit of the keyboard's dimensions.
	 * This determines the drawing size of the child views.
	 */
	private int unit = (int) getResources().getDisplayMetrics().density;

	private final MidiDriver midi;

	public PianoKeyboard(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutParams p;

		midi = new MidiDriver();

		PianoKey pb = new PianoKey(context, Pitch.of(PitchClass.C, 4)); // middle C
//		p = new LayoutParams(50*dp, 150*dp);
//		p.topMargin = 20*dp;
//		pb.setLayoutParams(p);
		addView(pb);

		pb = new PianoKey(context, Pitch.of(PitchClass.D, 4));
//		p = new LayoutParams(50*dp, 150*dp);
//		p.topMargin = 20*dp;
//		p.leftMargin = 60*dp;
//		pb.setLayoutParams(p);
		addView(pb);

		pb = new PianoKey(context, Pitch.of(PitchClass.E, 4));
//		p = new LayoutParams(50*dp, 150*dp);
//		p.topMargin = 20*dp;
//		p.leftMargin = 120*dp;
//		pb.setLayoutParams(p);
		addView(pb);

		pb = new PianoKey(context, Pitch.of(PitchClass.F, 4));
//		p = new LayoutParams(50*dp, 150*dp);
//		p.topMargin = 20*dp;
//		p.leftMargin = 180*dp;
//		pb.setLayoutParams(p);
		addView(pb);

		pb = new PianoKey(context, Pitch.of(PitchClass.G, 4));
//		p = new LayoutParams(50*dp, 150*dp);
//		p.topMargin = 20*dp;
//		p.leftMargin = 240*dp;
//		pb.setLayoutParams(p);
		addView(pb);

		pb = new PianoKey(context, Pitch.of(PitchClass.A, 4));
//		p = new LayoutParams(50*dp, 150*dp);
//		p.topMargin = 20*dp;
//		p.leftMargin = 300*dp;
//		pb.setLayoutParams(p);
		addView(pb);

		pb = new PianoKey(context, Pitch.of(PitchClass.B, 4));
//		p = new LayoutParams(50*dp, 150*dp);
//		p.topMargin = 20*dp;
//		p.leftMargin = 360*dp;
//		pb.setLayoutParams(p);
		addView(pb);

		pb = new PianoKey(context, Pitch.of(PitchClass.C, 5));
//		p = new LayoutParams(50*dp, 150*dp);
//		p.topMargin = 20*dp;
//		p.leftMargin = 420*dp;
//		pb.setLayoutParams(p);
		addView(pb);

		/*
		 * Make sure to create only one MidiDriver and start it only once
		 * since its creation or last call to stop().
		 * Otherwise the MIDI sound will stutter.
		 * (The current setup sounds fine at first, but breaks when the
		 * screen is rotated).
		 */
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
		private Paint pText;

		public PianoKey(Context context, Pitch pitch) {
			super(context);
			this.pitch = pitch;

			layout = new LayoutParams(width()*unit, height()*unit);
			layout.leftMargin = 60*unit*pitch.pitchClass().naturalPitchClass().ordinal()
					+ 420*unit*(pitch.octave() - 4);
			setLayoutParams(layout);

			p = new Paint(Paint.ANTI_ALIAS_FLAG);
			p.setColor(0xff7777dd);
			pText = new Paint(Paint.ANTI_ALIAS_FLAG);
			pText.setColor(0xffffffff);

			setOnTouchListener(new PianoKeyListener());
		}

		int width() {
			return 50;
		}

		int height() {
			return 150;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawRect(new Rect(0, 0, 50*unit, 150*unit), p);
//			canvas.drawText(Integer.toHexString(pitch), 10*dp, 70*dp, pText);
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
