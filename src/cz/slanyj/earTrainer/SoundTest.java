package cz.slanyj.earTrainer;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.*;
import javax.sound.midi.spi.MidiFileWriter;

import static javax.sound.midi.ShortMessage.*;
import static javax.sound.midi.MetaMessage.*;
import cz.slanyj.music.*;


class SoundTest {
	
//	private Receiver receiver;
	private Sequencer seq;
//	private Transmitter seqTrans;
//	private Synthesizer synth;
//	private Receiver synthRcvr;
	
	int channel = 0;
	int velocity = 93;
	
	SoundTest() {
		try {
//			receiver = MidiSystem.getReceiver();
			// Get the default sequencer and its transmitter
			seq = MidiSystem.getSequencer();
//			seqTrans = seq.getTransmitter();
			// Get the default synthesizer and its receiver
//			synth = MidiSystem.getSynthesizer();
//			synthRcvr = synth.getReceiver();
			// Connect the sequencer to the synthesizer
//			seqTrans.setReceiver(synthRcvr);			
		} catch (MidiUnavailableException e) {
			// TODO Write exception for unavailable MIDI
		}
	}
		
	void play() {
		try {
			Sequence sequence = new Sequence(Sequence.PPQ, 60);
			Track tr0 = sequence.createTrack();
		
			// Note start and end
			MidiEvent noteOn = new MidiEvent(new ShortMessage(NOTE_ON, channel, 60, velocity), 0);
			MidiEvent noteOff = new MidiEvent(new ShortMessage(NOTE_OFF, channel, 60, velocity), 60);
//			MetaMessage end = new MetaMessage(0x2F, new byte[]{}, 0);
//			MidiEvent endTrack = new MidiEvent(end, 4);
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
			
//			tr0.add(endTrack);
			
			
			seq.setTempoInBPM(120);
/*			boolean added = seq.addMetaEventListener(new MetaEventListener(){
				@Override
				public void meta(MetaMessage meta) {
					System.out.println("Calling meta.");
					if (meta.getType() == 0x2F) {seq.close();}
				}
			});*/
			
//			sequence = MidiSystem.getSequence(new File("test.mid"));
			
			seq.open();
			seq.setSequence(sequence);
			seq.addMetaEventListener(new Listener());
//			boolean added = seq.addMetaEventListener(new Listener());
			
//			if (added) {System.out.println("Anonymous listener added.");}
			
			seq.start();
			
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/* catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private class Listener implements MetaEventListener {
		@Override
		public void meta(MetaMessage meta) {
			System.out.println("Running listener meta.");
			if (meta.getType() == 0x2F) {
//				seq.stop();
				seq.close();
			}
		}
		Listener() {
			System.out.println("Listener added.");
		}
	}
	
	public static void main (String[] args) {
		SoundTest sound = new SoundTest();
		sound.play();
	}
	
}
