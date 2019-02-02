package com.github.singond.melodear.android;

import org.billthefarmer.mididriver.MidiDriver;

import com.github.singond.melodear.android.views.PianoKeyboard;
import com.github.singond.music.Pitch;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MelodicDictationActivity extends Activity {

	private static final String TAG = "MelodicDictation";

	private MidiDriver midi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Creating MelodicDictationActivity");

		/*
		 * Initialize the listener.
		 *
		 * Make sure to create only one MidiDriver and start it only once
		 * since its creation or last call to stop().
		 * Otherwise the MIDI sound will stutter.
		 * (The current setup sounds fine at first, but breaks when the
		 * screen is rotated).
		 */
		midi = new MidiDriver();
		midi.start();
		setContentView(R.layout.activity_melodict);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "Starting MelodicDictationActivity");

//		midi.start();
		PianoKeyboard kbd = (PianoKeyboard) findViewById(R.id.melodict_keyboard);
		kbd.setListener(new KeyboardListener(midi));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Destroying MelodicDictationActivity");
		midi.stop();
	}

	private static class KeyboardListener implements PianoKeyboard.Listener {

		private MidiDriver midi;

		public KeyboardListener(MidiDriver midi) {
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