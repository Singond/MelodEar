package cz.slanyj.earTrainer;

import java.util.HashSet;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import cz.slanyj.music.*;

class Melody implements MusicTask {
	
	int channel = Sound.channel;
	int velocity = Sound.velocity;
	static int crotchet = Sound.resolution;
	
	/** The melody itself as an array of notes. */
	XNote[] self;
	/** The length of the melody. */
	private int length;
	/**
	 * A set of numbers identifying the span of the melody.<p>
	 * <b>[0]:</b> Key step to start at (tonic = 1)<br>
	 * <b>[1]:</b> Key step to finish at (tonic = 1);<br>
	 * <b>[2]:</b> The octave of the low bound;<br>
	 * <b>[3]:</b> How many times the tonic is crossed when going from the bottom of the span up.
	 * Positive when going up, negative when going down.
	 */
	int[] span;
	/** The owning Trainer. */
	private Trainer trainer;
	/** Preferences */
	private Preferences pref;
	/** A generator of random numbers. */
	private Random rndGen = new Random();
	/** The position of the note to be checked against user input. */
	private int cursor = 0;
	
	/** A list of notes allowed in the melody. Will change with key. */
	static HashSet<Tone> allowed;
	
	/* CONSTRUCTORS */
	/**
	 * Create a random diatonic melody using all tones of the key.
	 * Each note is randomly selected independent of the previous notes. 
	 * @param key The key of the melody.
	 * @param prefs A Preferences object with the preferences to be used.
	 */
	Melody(Key key, Preferences prefs) {
		// Set associated trainer
//		this.trainer = trainer;
		// Get preferences and load them
		this.pref = prefs;
		loadPrefs(pref);
		// Create a scale between specified endpoints
		Note[] scale = key.scale(span[0], span[1], span[2], span[3]);
		// Create an empty array of the required length to hold the melody
		self = new XNote[length];
		// Fill it with XNotes
		for (int i=0; i<length; i++) {
			// A random index
			int rnd = rndGen.nextInt(scale.length);
			// Get the note at the index, make it into an XNote and add it to the melody
			self[i] = new XNote(scale[rnd]);
		}
	}
	
	/* METHODS */
	/** List all notes as a string. */
	void list() {
		for (XNote note : self) {
			System.out.println(note.name());
		}
	}
	/**
	 * Plays this melody in a new thread.
	 * This thread terminates only when the sequence is finished playing.
	 * This can be used to pause execution until the sequence finishes.
	 * @return The thread in which the sequence is played.
	 * 
	 */
	public Thread play() {
		try {
			// Create a blank sequence with division into 1/48 of a quarter note and one track.
			Sequence sequence = new Sequence(Sequence.PPQ, crotchet, 1);
			// Get the first track
			Track tr1 = sequence.getTracks()[0];
			// Add this chord to the start of the sequence with a length of one crotchet (quarter note)
			for (int i=0; i<self.length; i++) {
				new XNote(self[i]).addToTrack(tr1, crotchet*i, crotchet);
			}
			// Send the sequence to the player (Sound)
			return Sound.play(sequence);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	
	
	
	
	
	/**
	 * Checks user input and compares it to the melody.<p>
	 * The note to be checked is passed as an argument and compared to the
	 * note under the cursor. If the notes do not match, return 0 and reset cursor to zero.
	 * If the notes match, move the cursor to the next note and return 1 if more notes follow
	 * or return 2 if it was the last note in melody.
	 */
	public int check(Note input) {
		if (input.pitch!=self[cursor].pitch) {
			// The notes do not match
			cursor = 0;
			return 0;
		} else if (cursor < length-1) {
			// The notes match and the note is not last
			cursor++;
			return 1;
		} else {
			// The note is the last
			return 2;
		}
	}
	
	
	/**
	 * Load preferences from the trainer.
	 */
	private void loadPrefs(Preferences prefs) {
		length = prefs.melodyLength;
		span = prefs.melodySpan;
	}
}
