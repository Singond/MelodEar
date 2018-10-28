package com.github.singond.melodear.android.sound;

import org.billthefarmer.mididriver.MidiDriver;

import com.github.singond.music.Pitch;

public class MidiDriverSoundDevice implements SoundDevice {

	private MidiDriver midi;

	private static final byte MIDI_MSG_NOTE_ON = (byte) 0x90;
	private static final byte MIDI_MSG_NOTE_OFF = (byte) 0x80;

	@Override
	public void open() {
		midi = new MidiDriver();
		midi.start();
	}

	@Override
	public void close() {
		midi.stop();
	}

	@Override
	public void play(Pitch pitch) {
		noteOn(pitch);
	}

	@Override
	public void pitchOn(Pitch pitch) {
		noteOn(pitch);
	}

	@Override
	public void pitchOff(Pitch pitch) {
		noteOff(pitch);
	}


	private void noteOn(Pitch pitch) {
		byte[] msg;
		msg = new byte[3];
		msg[0] = MIDI_MSG_NOTE_ON;
		msg[1] = (byte)(pitch.midiNumber());
		msg[2] = (byte)(127); // max velocity
		midi.write(msg);
	}

	private void noteOff(Pitch pitch) {
		byte[] msg;
		msg = new byte[3];
		msg[0] = MIDI_MSG_NOTE_OFF;
		msg[1] = (byte)(pitch.midiNumber());
		msg[2] = (byte)(0); // max velocity
		midi.write(msg);
	}
}
