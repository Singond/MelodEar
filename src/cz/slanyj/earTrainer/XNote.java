package cz.slanyj.earTrainer;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import cz.slanyj.music.*;

class XNote extends Note {
	
	int channel = Sound.channel;
	int velocity = Sound.velocity;
	static int crotchet = Sound.resolution;

	// Own constructor
	XNote(Note note) {
		super(note.tone, note.octave);
	}
	
	
	/** Play this note without specifying its end. */
	void play() {
		// Use the play(note) method
		Sound.play(this);
	}
	
	/**
	 * Adds this note to a specified position in a specified track.
	 * 
	 * @param track (Track) Target MIDI track.
	 * @param position (int) The position (in ticks) from the start of the sequence.
	 * @param length (int) The length of the note in ticks.
	 * 
	 * @throws InvalidMidiDataException
	 */
	void addToTrack(Track track, int position, int length) throws InvalidMidiDataException {
		// Create the Note On and Note Off events
		MidiEvent noteOn = new MidiEvent(new ShortMessage(NOTE_ON, channel, pitch, velocity), position);
		MidiEvent noteOff = new MidiEvent(new ShortMessage(NOTE_OFF, channel, pitch, velocity), position+length);
		// Add the events to the target track
		track.add(noteOn);
		track.add(noteOff);
	}
	
	
}
