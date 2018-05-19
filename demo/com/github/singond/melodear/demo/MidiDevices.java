package com.github.singond.melodear.demo;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

public class MidiDevices {

	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException {
		Scanner scanner = new Scanner(System.in);
		MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < info.length; i++) {
			MidiDevice.Info dev = info[i];
			System.out.format("%02d  %s (ver. %s): %s\n", i, dev.getName(),
			                  dev.getVersion(), dev.getDescription());
		}
		System.out.print("\nEnter the number of a MIDI device to test: ");
		MidiDevice.Info deviceInfo = info[scanner.nextInt()];
		scanner.nextLine(); // Consume the newline after the number input
		MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
		Soundbank sb = null;
		if (device instanceof Synthesizer) {
//			Synthesizer synth = (Synthesizer) device;
			System.out.println("Enter path to sound font file:");
			String fileString = scanner.nextLine();
			if (!fileString.isEmpty()) {
				File sbFile = new File(fileString);
    			if (sbFile.exists()) {
    				try {
    					sb = MidiSystem.getSoundbank(sbFile);
    					displayInstruments(sb);
//    					synth.loadAllInstruments(sb);
    					System.out.println("Changing soundbank");
    				} catch (IOException e) {
    					System.out.println("Error when reading " + sbFile);
    				}
    			} else {
    				System.out.println("The file does not exist");
    			}
			} else {
				System.out.println("Not changing soundbank");
			}
		}
		Sequencer seq = MidiSystem.getSequencer();
		int tempo = 120;
		if (sb != null) {
			MidiSystemDemo.play(scale(), device, sb, tempo);
		} else {
			MidiSystemDemo.play(scale(), device, tempo);
		}
		seq.close();
		device.close();
		scanner.close();
    }
	
	private static Sequence scale() throws InvalidMidiDataException {
		int channel = 0;
		int velocity = 93;
		
		Sequence sequence = new Sequence(Sequence.PPQ, 60);
		Track tr0 = sequence.createTrack();
		
		int step = 60;
		for (int i = 0; i < 8; i++) {
			if (i == 3 || i == 7) {
				step += 1;
			} else if (i != 0) {
				step += 2;
			}
    		tr0.add(new MidiEvent(new ShortMessage(NOTE_ON, channel, step, velocity), 60*i));
    		tr0.add(new MidiEvent(new ShortMessage(NOTE_OFF, channel, step, velocity), 60*(i+1)));
		}
		
		return sequence;
	}
	
	private static void displayInstruments(Soundbank sb) {
		Instrument[] instruments = sb.getInstruments();
		System.out.println("Instruments available in " + sb.getName() + ":");
		for (Instrument i : instruments) {
			System.out.format("%03d %03d %s\n", i.getPatch().getBank(),
			                  i.getPatch().getProgram(), i.getName());
		}
	}
}
