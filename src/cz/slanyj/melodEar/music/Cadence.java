package cz.slanyj.melodEar.music;

import static cz.slanyj.music.OrientedInterval.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import cz.slanyj.music.ChordVoicing;
import cz.slanyj.music.Note;

import static cz.slanyj.music.Chord.Type.*;
import static cz.slanyj.music.Interval.C4;

public class Cadence {

	
	private final cz.slanyj.music.Cadence cadence;
	
	public Cadence(cz.slanyj.music.Cadence c) {
		this.cadence = c;
	}
	
	/**
	 * 
	 * @param tonic
	 * @param crotchet Duration of crotchet in ticks
	 * @return
	 */
	public Sequence getAsSequence(Note tonic, int crotchet) {
		try {
			// Create a blank sequence with resolution specified in the Sound static class
			// and a length of a quarter note and one track.
			Sequence sequence = new Sequence(Sequence.PPQ, crotchet, 1);
			// Get the first track
			Track tr1 = sequence.getTracks()[0];

			int i = 0;
			for (ChordVoicing chord : cadence.getCadence(tonic)) {
				new XChord(chord).addToTrack(tr1, i*crotchet, crotchet);
				i++;
			}	
			return sequence;
		} catch (InvalidMidiDataException e) {
			return null;
		}
	}
}
