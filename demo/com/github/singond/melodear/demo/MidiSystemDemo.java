package com.github.singond.melodear.demo;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

public class MidiSystemDemo {

	private static Sequence demoSequence() throws InvalidMidiDataException {
		int channel = 0;
		int velocity = 93;

		Sequence sequence = new Sequence(Sequence.PPQ, 60);
		Track tr0 = sequence.createTrack();

		// Note start and end
		MidiEvent noteOn = new MidiEvent(new ShortMessage(NOTE_ON, channel, 60, velocity), 0);
		MidiEvent noteOff = new MidiEvent(new ShortMessage(NOTE_OFF, channel, 60, velocity), 60);
		tr0.add(noteOn);
		tr0.add(noteOff);

		noteOn = new MidiEvent(new ShortMessage(NOTE_ON, channel, 64, velocity), 60);
		noteOff = new MidiEvent(new ShortMessage(NOTE_OFF, channel, 64, velocity), 120);
		tr0.add(noteOn);
		tr0.add(noteOff);

		noteOn = new MidiEvent(new ShortMessage(NOTE_ON, channel, 67, velocity), 120);
		noteOff = new MidiEvent(new ShortMessage(NOTE_OFF, channel, 67, velocity), 180);
		tr0.add(noteOn);
		tr0.add(noteOff);

		return sequence;
	}

	public static void play(Sequence sequence, MidiDevice device, Soundbank soundbank, int tempo) {
		playWithSoundbank(sequence, device, soundbank, tempo, false);
	}

	public static void play(Sequence sequence, MidiDevice device, int tempo) {
		playWithSettings(sequence, device, tempo, false);
	}

	public static void play(Sequence sequence, int tempo) {
		playWithSettings(sequence, null, tempo, false);
	}

	public static void play(Sequence sequence) {
		playWithSettings(sequence, null, 120, false);
	}

	/**
	 * Plays a single note directly.
	 */
	public static void play(int pitch, Soundbank soundbank) {
		playWithSoundbank(pitch, soundbank);
	}
	private static void playWithSettings(Sequence sequence, MidiDevice device, int tempo, boolean dbg) {
		try (Sequencer seq = MidiSystem.getSequencer()) {
			if (device != null) {
				if (!device.isOpen()) {
					System.out.println("Opening device " + device.getDeviceInfo().getName());
					System.out.println(device.getClass());
					device.open();
				}
				seq.getTransmitter().setReceiver(device.getReceiver());
			}
			seq.setTempoInBPM(tempo);
			playSequence(sequence, seq, dbg);
		} catch (MidiUnavailableException e) {
			System.out.println("No MIDI sequencer available");
			e.printStackTrace();
		}
	}

	private static void playWithSoundbank(Sequence sequence, MidiDevice device, Soundbank sb,
			int tempo, boolean dbg) {
		try (Sequencer seq = MidiSystem.getSequencer()) {
			Synthesizer synth = null;
			if (device != null) {
				if (!device.isOpen()) {
					System.out.println("Opening device " + device.getDeviceInfo().getName());
					System.out.println(device.getClass());
					device.open();
				}
				if (device instanceof Synthesizer) {
					synth = (Synthesizer) device;
					synth.open();
					System.out.println("Unloading default instruments...");
					synth.unloadAllInstruments(synth.getDefaultSoundbank());
					System.out.println("Loading instruments from the soundbank...");
					synth.loadAllInstruments(sb);
				} else {
					System.out.println("The device is not a synthesizer: cannot change sound bank");
					synth = MidiSystem.getSynthesizer();
				}
				seq.getTransmitter().setReceiver(device.getReceiver());
			}
			seq.setTempoInBPM(tempo);
			playSequence(sequence, seq, dbg);
			if (synth != null) {
				synth.close();
			}
		} catch (MidiUnavailableException e) {
			System.out.println("No MIDI sequencer available");
			e.printStackTrace();
		}
	}

	private static void playWithSoundbank(int pitch, Soundbank sb) {
		try (Synthesizer synth = MidiSystem.getSynthesizer();
				Receiver receiver = synth.getReceiver();) {
			synth.open();
			if (sb != null) {
				System.out.println("Unloading default instruments...");
				synth.unloadAllInstruments(synth.getDefaultSoundbank());
				System.out.println("Loading instruments from the soundbank...");
				synth.loadAllInstruments(sb);
			}
//			Thread.sleep(100);
			int channel = 0;
			int velocity = 93;
			receiver.send(new ShortMessage(
					ShortMessage.NOTE_ON, channel, pitch, velocity), -1);
			Thread.sleep(4000);
		} catch (MidiUnavailableException e) {
			System.out.println("No MIDI sequencer available");
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			System.out.println("Invalid MIDI data sent");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
			e.printStackTrace();
		}
	}

	private static void playSequence(Sequence sequence, Sequencer seq, boolean dbg) {
		try {
			seq.open();
			System.out.println("Opening sequencer...");
			Thread.sleep(2000);
			seq.setSequence(sequence);
			EndOfTrackListener l = new EndOfTrackListener(seq);
			l.useLog(dbg);
			seq.addMetaEventListener(l);
			System.out.println("Starting playback");
			seq.start();
			synchronized (seq) {
				while (seq.isRunning()) {
					if (dbg) System.out.println("Waiting");
					seq.wait();
				}
			}
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			System.out.println("No MIDI sequencer available");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Sequencer interrupted");
			e.printStackTrace();
		}
		if (dbg) System.out.println("Finished");
	}

	private static Sequence sequence(int... notes) throws InvalidMidiDataException {
		int channel = 0;
		int velocity = 93;

		Sequence sequence = new Sequence(Sequence.PPQ, 60);
		Track tr0 = sequence.createTrack();

		for (int i = 0; i < notes.length; i++) {
			tr0.add(new MidiEvent(new ShortMessage(NOTE_ON, channel, notes[i], velocity), 60*i));
			tr0.add(new MidiEvent(new ShortMessage(NOTE_OFF, channel, notes[i], velocity), 60*(i+1)));
		}

		return sequence;
	}

	private static class EndOfTrackListener implements MetaEventListener {

		private final Sequencer seq;
		private boolean log = false;

		private EndOfTrackListener(Sequencer seq) {
			this.seq = seq;
			if (log) System.out.println("Listening for end of track");
		}

		@Override
		public void meta(MetaMessage meta) {
			if (log) System.out.println("Meta message: 0x"
			         + Integer.toHexString(meta.getType()));
			if (meta.getType() == 0x2F) {
				if (log) System.out.println("End of track");
				synchronized (seq) {
					seq.stop();
					seq.notifyAll();
				}
			}
		}

		public void useLog(boolean log) {
			this.log = log;
		}
	}

	public static void main (String[] args) throws InvalidMidiDataException {
		play(demoSequence());
	}
}
