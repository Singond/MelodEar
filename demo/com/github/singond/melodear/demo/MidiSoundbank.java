package com.github.singond.melodear.demo;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;

public class MidiSoundbank {

	public static void singleNote() throws MidiUnavailableException, InvalidMidiDataException {
		Scanner scanner = new Scanner(System.in);
		Soundbank sb = null;
		System.out.println("Enter path to sound font file:");
		String fileString = scanner.nextLine();
		if (!fileString.isEmpty()) {
			File sbFile = new File(fileString);
			if (sbFile.exists()) {
				try {
					sb = MidiSystem.getSoundbank(sbFile);
					displayInstruments(sb);
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
		MidiSystemDemo.play(55, sb);
		scanner.close();
	}

	private static void displayInstruments(Soundbank sb) {
		Instrument[] instruments = sb.getInstruments();
		System.out.println("Instruments available in " + sb.getName() + ":");
		for (Instrument i : instruments) {
			System.out.format("%03d %03d %s\n", i.getPatch().getBank(),
			                  i.getPatch().getProgram(), i.getName());
		}
	}

	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException {
		singleNote();
	}
}
