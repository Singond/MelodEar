package com.github.singond.melodear.demo;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;

import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
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
		MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
		Sequencer seq = MidiSystem.getSequencer();
		MidiSystemDemo.play(scale(), device, 120);
		seq.close();
		device.close();
		scanner.close();
    }
	
	private static Sequence scale() throws InvalidMidiDataException {
		int channel = 0;
		int velocity = 93;
		
		Sequence sequence = new Sequence(Sequence.PPQ, 60);
		Track tr0 = sequence.createTrack();
		
		for (int i = 0; i < 8; i++) {
    		tr0.add(new MidiEvent(new ShortMessage(NOTE_ON, channel, 60+i, velocity), 60*i));
    		tr0.add(new MidiEvent(new ShortMessage(NOTE_OFF, channel, 60+i, velocity), 60*(i+1)));
		}
		
		return sequence;
	}
}
