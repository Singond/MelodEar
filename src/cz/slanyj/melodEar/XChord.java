package cz.slanyj.melodEar;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;
import cz.slanyj.music.*;
import cz.slanyj.music.Chord.Chords;

import javax.sound.midi.*;

/** A musical chord, an extension of the Chord class.
 * This includes methods to play itself.
 * @author Sorondil
 *
 */
public class XChord extends Chord {

	int channel = Sound.channel;
	int velocity = Sound.velocity;
	/** The length of a crotchet in ticks. */
	static int crotchet = Sound.resolution;
	
	XNote[] voicing;
	
	XChord(Note base, Chords type) {
		super(base, type);
		voicing = new XNote[super.size];
		// Recast the Notes into XNotes
		for (int i=0; i<super.voicing.length; i++) {
			this.voicing[i] = new XNote(super.voicing[i]);
		}
	}
	XChord(Note base, Chords type, int inversion) {
		super(base, type, inversion);
		voicing = new XNote[super.size];
		// Recast the Notes into XNotes
		for (int i=0; i<super.voicing.length; i++) {
			this.voicing[i] = new XNote(super.voicing[i]);
		}
	}
	XChord(Note base, int inversion, Interval... structure) {
		super(base, inversion, structure);
		voicing = new XNote[super.size];
		// Recast the Notes into XNotes
		for (int i=0; i<super.voicing.length; i++) {
			this.voicing[i] = new XNote(super.voicing[i]);
		}
	}
	
	/**
	 * Plays the chord in the length of one crotchet.<p>
	 * Constructs a new Sequence object and sends it to the Sound class.
	 * 
	 */
	void play() {
		try {
			// Create a blank sequence with division into 1/48 of a quarter note and one track.
			Sequence sequence = new Sequence(Sequence.PPQ, crotchet, 1);
			// Get the first track
			Track tr1 = sequence.getTracks()[0];
			// Add this chord to the start of the sequence with a length of one crotchet (quarter note)
			addToTrack(tr1, 0, crotchet);
			// Send the sequence to the player (Sound)
			Sound.play(sequence);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Adds this chord to a specified position in a specified track.<p>
	 * Adds all individual notes to the track to be played simultaneously.
	 * @param track (Track) Target MIDI track.
	 * @param position (int) The position (in ticks) from the start of the sequence.
	 * @param length (int) The length of the note in ticks.
	 */
	void addToTrack(Track track, int position, int length) throws InvalidMidiDataException {
		// Add each note to the track
		for (XNote note : voicing) {
			note.addToTrack(track, position, length);
		}
	}
	
	
}
