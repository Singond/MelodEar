package com.github.singond.melodear.demo;

import javax.sound.midi.*;
import static javax.sound.midi.ShortMessage.*;

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
	
	public static void play(Sequence sequence) {
		try (Sequencer seq = MidiSystem.getSequencer()) {
			seq.setTempoInBPM(120);
			seq.open();
			seq.setSequence(sequence);
			seq.addMetaEventListener(new EndOfTrackListener(seq));
			seq.start();
			synchronized (seq) {
				while (seq.isRunning()) {
					System.out.println("Waiting");
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
		System.out.println("Finished");
	}
	
	private static class EndOfTrackListener implements MetaEventListener {
		
		private final Sequencer seq;
		
		private EndOfTrackListener(Sequencer seq) {
			this.seq = seq;
			System.out.println("Listening for end of track");
		}
		
		@Override
		public void meta(MetaMessage meta) {
			System.out.println("Meta message: 0x"
					+ Integer.toHexString(meta.getType()));
			if (meta.getType() == 0x2F) {
				System.out.println("End of track");
				synchronized (seq) {
					seq.stop();
					seq.notifyAll();
				}
			}
		}
	}
	
	public static void main (String[] args) throws InvalidMidiDataException {
		play(demoSequence());
	}
}
