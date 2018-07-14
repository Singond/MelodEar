package com.github.singond.android.views;

import org.billthefarmer.mididriver.MidiDriver;

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

	private static final int whiteKeyWidth = 24;
	private static final int blackKeyWidth = 14;
	private static final int whiteKeyHeight = 155;
	private static final int blackKeyHeight = 103;
	private static final int octaveWidth = 168;

	@Deprecated
	private final int dp = (int) getResources().getDisplayMetrics().density;
	private final int scale = (int) getResources().getDisplayMetrics().density;

	private final MidiDriver midi;

	public PianoKeyboard(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutParams p;

		midi = new MidiDriver();

		PianoKey pb = new PianoKey(context, 60, midi); // middle C
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		pb.setLayoutParams(p);
		addView(pb);

		pb = new PianoKey(context, 62, midi);
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		p.leftMargin = 60*dp;
		pb.setLayoutParams(p);
		addView(pb);

		pb = new PianoKey(context, 64, midi);
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		p.leftMargin = 120*dp;
		pb.setLayoutParams(p);
		addView(pb);

		pb = new PianoKey(context, 65, midi);
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		p.leftMargin = 180*dp;
		pb.setLayoutParams(p);
		addView(pb);

		pb = new PianoKey(context, 67, midi);
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		p.leftMargin = 240*dp;
		pb.setLayoutParams(p);
		addView(pb);

		pb = new PianoKey(context, 69, midi);
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		p.leftMargin = 300*dp;
		pb.setLayoutParams(p);
		addView(pb);

		pb = new PianoKey(context, 71, midi);
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		p.leftMargin = 360*dp;
		pb.setLayoutParams(p);
		addView(pb);

		pb = new PianoKey(context, 72, midi);
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		p.leftMargin = 420*dp;
		pb.setLayoutParams(p);
		addView(pb);

		/*
		 * Make sure to create only one MidiDriver and start it only once
		 * since its creation or last call to stop().
		 * Otherwise the MIDI sound will stutter.
		 * (The current setup sounds fine at first, but breaks when the
		 * screen is rotated).
		 */
		midi.start();
	}

	private class PianoKey extends View {

		private final int pitch;

		private LayoutParams layout;
		private Paint p;
		private Paint pText;

		public PianoKey(Context context, int pitch, MidiDriver midi) {
			super(context);
			this.pitch = pitch;
			if (midi == null) {
				throw new NullPointerException("MIDI driver is null");
			}

			p = new Paint(Paint.ANTI_ALIAS_FLAG);
			p.setColor(0xff7777dd);
			pText = new Paint(Paint.ANTI_ALIAS_FLAG);
			pText.setColor(0xffffffff);

//			player = new Player(pitch, midi);
//			setOnTouchListener(player);
			setOnTouchListener(new PianoKeyListener());
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawRect(new Rect(0, 0, 50*dp, 150*dp), p);
//			canvas.drawText(Integer.toHexString(pitch), 10*dp, 70*dp, pText);
		}

		private class PianoKeyListener implements OnTouchListener {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						Log.d(TAG, "Pressed key " + pitch);
						byte[] msg;
						msg = new byte[3];
						msg[0] = (byte)(0x90); // note on
						msg[1] = (byte)(pitch);
						msg[2] = (byte)(127); // max velocity
						midi.write(msg);
//						if (listener != null) {
//							listener.keyPressed(pitch);
//						}
						return true;
					case MotionEvent.ACTION_UP:
						Log.d(TAG, "Released key " + pitch);
						msg = new byte[3];
						msg[0] = (byte)(0x80); // note off
						msg[1] = (byte)(pitch);
						msg[2] = (byte)(0); // max velocity
						midi.write(msg);
//						if (listener != null) {
//							listener.keyReleased(pitch);
//						}
						return true;
				}
				return false;
			}
		}
	}
}
