package com.github.singond.melodear.android.views;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.singond.melodear.PitchParser;
import com.github.singond.melodear.ScientificPitchParser;
import com.github.singond.melodear.android.R;
import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;
import com.github.singond.music.Pitches;

import android.content.Context;
import android.content.res.TypedArray;
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

	private static final PitchParser pitchParser = new ScientificPitchParser();

	private Listener listener;

	/** Horizontal offset of the start key and C0. */
	private int startOffset;

	/**
	 * Horizontal unit of the keyboard's dimensions.
	 * This determines the horizontal drawing size of the child views.
	 */
	private float hunit = 2 * getResources().getDisplayMetrics().density;

	/**
	 * Vertical unit of the keyboard's dimensions.
	 * This determines the vertical drawing size of the child views.
	 */
	private float vunit = 1.5f * getResources().getDisplayMetrics().density;

	private Paint whiteKeyFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint blackKeyFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint pressedKeyFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint keyBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	{
		whiteKeyFillPaint.setStyle(Paint.Style.FILL);
		whiteKeyFillPaint.setColor(0xffffffff);

		blackKeyFillPaint.setStyle(Paint.Style.FILL);
		blackKeyFillPaint.setColor(0xff202020);

		pressedKeyFillPaint.setStyle(Paint.Style.FILL);
		pressedKeyFillPaint.setColor(0xff606060);

		keyBorderPaint.setStyle(Paint.Style.STROKE);
		keyBorderPaint.setColor(0xff404040);
	}

	public PianoKeyboard(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray ta = context.getTheme().obtainStyledAttributes(
				attrs, R.styleable.PianoKeyboard, 0, 0);
		Pitch start, end;
		try {
			String startPitch = null, endPitch = null;
			startPitch = ta.getString(R.styleable.PianoKeyboard_lowestKey);
			endPitch = ta.getString(R.styleable.PianoKeyboard_highestKey);
			start = parsePitch(startPitch);
			end = parsePitch(endPitch);
		} finally {
			ta.recycle();
		}
		Log.d(TAG, "Keyboard range: " + start + "-" + end);

		startOffset = keyOffset(start);
		populate(start, end, context);
	}

	private static Pitch parsePitch(String string) {
		// TODO Consider not depending on MelodEar internals here
		// (implement pitch parsing privately here)
		return pitchParser.parse(string);
	}

	private void populate(Pitch start, Pitch end, Context context) {
		// TODO Ensure the endpoints are white keys
		// Draw white keys first to make them draw below the black keys
		List<Pitch> keyPitches = Pitches.allBetween(start, end, whiteKeys);
		for (Pitch pitch : keyPitches) {
			addView(new WhiteKey(context, pitch));
		}
		keyPitches = Pitches.allBetween(start, end, blackKeys);
		for (Pitch pitch : keyPitches) {
			addView(new BlackKey(context, pitch));
		}
	}

	/**
	 * Assigns the given object to listen for events in this keyboard.
	 *
	 * @param listener the object to listen for keyboard events
	 */
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Although the keyboard is similar to a list, its musical nature
	 * requires that the response to a key press be as quick as possible,
	 * therefore this implementation always return false.
	 *
	 * @return always {@code false}
	 */
	// Can't see much difference though
	@Override
	public boolean shouldDelayChildPressedState() {
		return false;
	}

	/**
	 * Returns the unscaled horizontal offset of a piano key of the
	 * given pitch from the C0 key.
	 *
	 * @param pitch the pitch of the key
	 * @return offset of key {@code pitch} and C0
	 */
	private int keyOffset(Pitch pitch) {
		return keyOffsets.get(pitch.pitchClass())
				+ octaveWidth * pitch.octave();
	}

	private abstract class PianoKey extends View {

		protected final Pitch pitch;

		private LayoutParams layout;

		private boolean pressed = false;

		public PianoKey(Context context, Pitch pitch) {
			super(context);
			this.pitch = pitch;

			layout = new LayoutParams(Math.round(width() * hunit),
			                          Math.round(height() * vunit));
			layout.leftMargin = Math.round((position() - startOffset) * hunit);
			setLayoutParams(layout);

			setOnTouchListener(new PianoKeyListener());
		}

		/**
		 * Returns the unscaled width of this key.
		 *
		 * @return the width of this key in relative measure
		 */
		protected abstract int width();

		/**
		 * Returns the unscaled height of this key.
		 * <p>
		 * Note that the word <em>height</em> refers to the display here.
		 * In real world, this would correspond to the <em>length</em>
		 * of the key.
		 *
		 * @return the height of this key in relative measure
		 */
		protected abstract int height();

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
		protected abstract int position();

		protected final boolean pressed() {
			return pressed;
		}

		@Override
		protected final void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			draw(canvas, hunit, vunit);
		}

		protected abstract void draw(Canvas canvas, float hunit, float vunit);

		private class PianoKeyListener implements OnTouchListener {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
//						Log.d(TAG, "Pressed key " + pitch);
						pressed = true;
						if (listener != null) {
							listener.keyPressed(pitch);
						}
						invalidate();
						return true;
					case MotionEvent.ACTION_UP:
//						Log.d(TAG, "Released key " + pitch);
						pressed = false;
						if (listener != null) {
							listener.keyReleased(pitch);
						}
						invalidate();
						return true;
				}
				return false;
			}
		}
	}

	private class WhiteKey extends PianoKey {

		WhiteKey(Context context, Pitch pitch) {
			super(context, pitch);
		}

		@Override
		protected int width() {
			return whiteKeyWidth;
		}

		@Override
		protected int height() {
			return whiteKeyHeight;
		}

		@Override
		protected int position() {
			return keyOffset(pitch);
		}

		@Override
		protected void draw(Canvas canvas, float hunit, float vunit) {
			canvas.drawRect(new Rect(0, 0, Math.round(width() * hunit),
					Math.round(height() * vunit)),
					pressed() ? pressedKeyFillPaint : whiteKeyFillPaint);
			canvas.drawRect(new Rect(0, 0, Math.round(width() * hunit),
					Math.round(height() * vunit)), keyBorderPaint);
		}
	}

	private class BlackKey extends PianoKey {

		BlackKey(Context context, Pitch pitch) {
			super(context, pitch);
		}

		@Override
		protected int width() {
			return blackKeyWidth;
		}

		@Override
		protected int height() {
			return blackKeyHeight;
		}

		@Override
		protected int position() {
			return keyOffset(pitch);
		}

		@Override
		protected void draw(Canvas canvas, float hunit, float vunit) {
			canvas.drawRect(new Rect(0, 0, Math.round(width() * hunit),
					Math.round(height() * vunit)),
					pressed() ? pressedKeyFillPaint : blackKeyFillPaint);
			canvas.drawRect(new Rect(0, 0, Math.round(width()*hunit),
					Math.round(height()*vunit)), keyBorderPaint);
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
}
