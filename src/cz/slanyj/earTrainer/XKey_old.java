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
class XKey_old {
	
	int channel = Sound.channel;
	int velocity = Sound.velocity;
	int crotchet = Sound.resolution;
	
	Key key;
	
	/** The base of the key, ie. tonic. */
	Tone base;
	/** The scale in the key implemented as CircList with the notes sorted in ascending order,
	 * starting with [0] = tonic. */
	CircList<Tone> scale;
	
	XKey_old(Key key) {
		this.key = key;
		scale = new CircList(key.scale);
	}
	
	/**
	 * Play the scale up starting in a given octave.
	 * @param octave
	 */
	void playScaleUp(int octave) {
		try {
			// Create a blank sequence with division into 1/48 of a quarter note and one track.
			Sequence sequence = new Sequence(Sequence.PPQ, crotchet, 1);
			// Get the first track
			Track tr1 = sequence.getTracks()[0];
			// Add this chord to the start of the sequence with a length of one crotchet (quarter note)
			for (int i=0; i<scale.size(); i++) {
				new XNote(new Note(scale.get(i), octave)).addToTrack(tr1, crotchet*i, crotchet);
			}
			// Send the sequence to the player (Sound)
			Sound.play(sequence);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * Plays a Major cadence: I-IV-V-I
	 * 
	 */
	void playCadence() {
		try {
			// Create a blank sequence with resolution specified in the Sound static class
			// and a length of a quarter note and one track.
			Sequence sequence = new Sequence(Sequence.PPQ, crotchet, 1);
			// Get the first track
			Track tr1 = sequence.getTracks()[0];
			// Create and add I (twice: to the beginning and end)
			Note tonic = new Note(scale.get(0), 4);
			XChord newChord = new XChord(tonic, MAJ);
			newChord.addToTrack(tr1, 0, crotchet);
			newChord.addToTrack(tr1, 3*crotchet, crotchet);
			// Create IV
			newChord = new XChord(tonic.up(C4), MAJ);
			newChord.addToTrack(tr1, crotchet, crotchet);
			// Create V
			newChord = new XChord(tonic.up(C5), MAJ);
			newChord.addToTrack(tr1, 2*crotchet, crotchet);
			
			// Send the sequence to the player (Sound)
			Sound.play(sequence);
		} catch (InvalidMidiDataException e) {
			
		}
	}
	
	
	
	
	
}
