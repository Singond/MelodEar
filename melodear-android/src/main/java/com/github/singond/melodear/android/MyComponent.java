package com.github.singond.melodear.android;

import java.io.IOException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.billthefarmer.mididriver.MidiDriver;

public class MyComponent extends RelativeLayout {

	private static final String TAG = "MyComponent";
	private final int dp = (int) getResources().getDisplayMetrics().density;

	private TextView text;

	private final MidiDriver midi;

	public MyComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutParams p;

		midi = new MidiDriver();

		PlayButton pb = new PlayButton(context, 60, midi); // middle C
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		pb.setLayoutParams(p);
		addView(pb);

		pb = new PlayButton(context, 62, midi);
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		p.leftMargin = 60*dp;
		pb.setLayoutParams(p);
		addView(pb);

		pb = new PlayButton(context, 64, midi);
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		p.leftMargin = 120*dp;
		pb.setLayoutParams(p);
		addView(pb);

		pb = new PlayButton(context, 65, midi);
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		p.leftMargin = 180*dp;
		pb.setLayoutParams(p);
		addView(pb);

		pb = new PlayButton(context, 67, midi);
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		p.leftMargin = 240*dp;
		pb.setLayoutParams(p);
		addView(pb);

		pb = new PlayButton(context, 69, midi);
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		p.leftMargin = 300*dp;
		pb.setLayoutParams(p);
		addView(pb);

		pb = new PlayButton(context, 71, midi);
		p = new LayoutParams(50*dp, 150*dp);
		p.topMargin = 20*dp;
		p.leftMargin = 360*dp;
		pb.setLayoutParams(p);
		addView(pb);

		pb = new PlayButton(context, 72, midi);
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
		this.text = text;
	}

	private class PlayButton extends View {

		private Paint p;
		private Paint pText;
		private final int pitch;
		private Player player;

		public PlayButton(Context context, int pitch, MidiDriver midi) {
			super(context);
			this.pitch = pitch;
			if (midi == null) {
				throw new NullPointerException("MIDI driver is null");
			}
			init(midi);
		}

		private void init(MidiDriver midi) {
			p = new Paint(Paint.ANTI_ALIAS_FLAG);
			p.setColor(0xff7777dd);
			pText = new Paint(Paint.ANTI_ALIAS_FLAG);
			pText.setColor(0xffffffff);
			initMidi(midi);
		}

		private void initMidi(MidiDriver midi) {
			player = new Player(pitch, midi);
			setOnTouchListener(player);
		}

		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawRect(new Rect(0, 0, 50*dp, 150*dp), p);
//			canvas.drawText(Integer.toHexString(pitch), 10*dp, 70*dp, pText);
		}
	}

	private static class Player implements OnTouchListener {

		private final int pitch;
		private final MidiDriver midi;

		Player(int pitch, MidiDriver midi) {
			this.pitch = pitch;
			this.midi = midi;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			byte[] msg;
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Log.v(TAG, "Pressed key of pitch " + pitch);
					msg = new byte[3];
					msg[0] = (byte)(0x90); // note on
					msg[1] = (byte)(pitch);
					msg[2] = (byte)(127); // max velocity
					midi.write(msg);
//					break;
					return true;
				case MotionEvent.ACTION_UP:
					Log.v(TAG, "Released key of pitch " + pitch);
					msg = new byte[3];
					msg[0] = (byte)(0x80); // note off
					msg[1] = (byte)(pitch);
					msg[2] = (byte)(0); // max velocity
					midi.write(msg);
//					break;
					return true;
			}
			return false;
		}

	}
}
