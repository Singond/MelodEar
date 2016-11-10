package cz.slanyj.earTrainer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import cz.slanyj.collections.CircList;
import cz.slanyj.music.*;

import static cz.slanyj.music.Chord.Chords.*;
import static cz.slanyj.music.Interval.*;

/**
 * A holder for a musical key (specified as enum cz.slanyj.music.Key).<p>
 * Keeps
 * @author Sorondil
 *
 */
class XKey extends Key {
	
	int channel = Sound.channel;
	int velocity = Sound.velocity;
	static int crotchet = Sound.resolution;
	
	XNote[] longScale;
	
	Sequence cadence;
	
	Key key;
	
	/** The base of the key, ie. tonic. */
	Tone base;
	/**
	 * The scale in the key implemented as CircList with the notes sorted in ascending order,
	 * starting with [0] = tonic.
	 */
	
	XKey(Key key) {
		super(key.tonic, key.mode);
	}
	
	/**
	 * Play the scale up starting in a given octave.
	 * @param octave
	 */
	void playScaleUp(int from, int to, int fromOctave, int toOctave) {
		try {
			// Create a blank sequence with division into 1/48 of a quarter note and one track.
			Sequence sequence = new Sequence(Sequence.PPQ, crotchet, 1);
			// Get the first track
			Track tr1 = sequence.getTracks()[0];
			// Add this chord to the start of the sequence with a length of one crotchet (quarter note)
			Note[] newScale = scale(from, to, fromOctave, toOctave);
			for (int i=0; i<newScale.length; i++) {
				new XNote(newScale[i]).addToTrack(tr1, crotchet*i, crotchet);
			}
			// Send the sequence to the player (Sound)
			Sound.play(sequence);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * Prepares cadence playback
	 */
	void prepareCadence() {
		if (cadence == null) {
			Sequence sequence = mode.cadence.getCadence(new Note(scale.get(0), 4), crotchet);
			Sound.prepare(sequence);
			cadence = sequence;
		}
	}
	/**
	 * Plays a Major cadence I-IV-V-I in a new thread.
	 * This thread terminates only when the sequence is finished playing.
	 * This can be used to pause execution until the sequence finishes.
	 * @return The thread in which the sequence is played.
	 * Terminates when the sequence end is reached.
	 */
	Thread playCadence() {
		/*try {
			// Create a blank sequence with resolution specified in the Sound static class
			// and a length of a quarter note and one track.
			Sequence sequence = new Sequence(Sequence.PPQ, crotchet, 1);
			// Get the first track
			Track tr1 = sequence.getTracks()[0];
			// Create and add I (twice: to the beginning and end)
			Note tonic = new Note(scale.get(0), 4);
			XChord newChord = new XChord(tonic, MAJ, 1);
			newChord.addToTrack(tr1, 0, crotchet);
			newChord.addToTrack(tr1, 3*crotchet, crotchet);
			// Create IV
			newChord = new XChord(tonic.up(C4), MAJ);
			newChord.addToTrack(tr1, crotchet, crotchet);
			// Create V
			newChord = new XChord(tonic.down(C4), MAJ, 2);
			newChord.addToTrack(tr1, 2*crotchet, crotchet);
			
			// Send the sequence to the player (Sound) and return its thread
			return Sound.play(sequence);
		} catch (InvalidMidiDataException e) {
			return null;
		}*/
		prepareCadence();
		try {
			return Sound.play(cadence);
		} catch (NullPointerException e) {
			throw new IllegalStateException("Cadence has been prepared but remains null");
		}
	}
	
	
	
	
	
}
