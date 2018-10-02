package com.github.singond.melodear.android;

import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import cn.sherlock.com.sun.media.sound.SF2Soundbank;
import cn.sherlock.com.sun.media.sound.SoftSynthesizer;
import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;
import jp.kshoji.javax.sound.midi.Receiver;
import jp.kshoji.javax.sound.midi.ShortMessage;
import jp.kshoji.javax.sound.midi.SysexMessage;

public class MainActivity extends DebugActivity {

	private static final String TAG = "MelodEar";

	private SoftSynthesizer synth;
	private Receiver recv;

	private static final int CONTROLLER_EXPRESSION = 11;
	private static final int CONTROLLER_CHANNEL_VOLUME = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			SF2Soundbank sf = new SF2Soundbank(getAssets().open("SmallTimGM6mb.sf2"));
//			SF2Soundbank sf = new SF2Soundbank(getAssets().open("GeneralUser_GS_MuseScore v1.442.sf2"));
			synth = new SoftSynthesizer();
			synth.open();
			synth.loadAllInstruments(sf);
			synth.getChannels()[0].programChange(0);
			recv = synth.getReceiver();
			ShortMessage msg = new ShortMessage();
			msg.setMessage(ShortMessage.CONTROL_CHANGE, 0, CONTROLLER_EXPRESSION, 127);
			msg.setMessage(ShortMessage.CONTROL_CHANGE, 0, CONTROLLER_CHANNEL_VOLUME, 127);
			recv.send(msg, -1);
			SysexMessage msg2 = new SysexMessage();
			byte[] volumeChange = {(byte) 0x70, 0x7F, 0x7F, 0x04, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xF7};
			msg2.setMessage(volumeChange, volumeChange.length);
			recv.send(msg2, -1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}

	}

	public void startMelodicDictation(@SuppressWarnings("unused") View view) {
		Intent intent = new Intent(this, MelodicDictationActivity.class);
		startActivity(intent);
	}

	public void soundTest(@SuppressWarnings("unused") View view) {
		Log.d(TAG, "Sound test: playing C major chord");
		long start = System.currentTimeMillis();
		try {
			Log.d(TAG, "Synth latency: " + synth.getLatency());
			ShortMessage msg = new ShortMessage();
			msg.setMessage(ShortMessage.NOTE_ON, 0, 60, 127);
			recv.send(msg, -1);
			msg.setMessage(ShortMessage.NOTE_ON, 0, 64, 127);
			recv.send(msg, -1);
			msg.setMessage(ShortMessage.NOTE_ON, 0, 67, 127);
			recv.send(msg, -1);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		Log.d(TAG, "Sound test: finished playing C major chord in " + (end - start) + " ms");
	}
}