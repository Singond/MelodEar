package cz.slanyj.music;

import static cz.slanyj.music.Interval.*;

/**
 * A musical chord. Can be specified generally, using only pitch classes (using Tone objects),
 * or exactly, ie. with exact pitches (using Note objects).
 * 
 * @author Sorondil
 *
 * @param size (int) The number of tones or notes.
 * @param tones (Tone[]) The tones comprising the chord.
 * @param voicing (Note[]) The voicing of the chord in exact notes.
 * @param hasVoicing (boolean) Indicates whether voicing has been specified, ie. whether <strong>notes</strong> is defined.
 *
 */

public class Chord {
	
	/** A database of available chord types. */
	public enum Chords {
		MAJ ("", "major triad", V3, M3),
		MIN ("", "minor triad", M3, V3);
		
		String symbol;
		String name;
		Interval[] intervals;
		private int size;
		
		Chords(String symbol, String name, Interval... intervals) {
			this.symbol = symbol;
			this.name = name;
			this.intervals = intervals;
			this.size = intervals.length + 1;
		}
		
	}
	
	protected int size;
	/** General tones comprising this chord. */
	Tone[] tones;
	/** The voicing of this chord. */
	protected Note[] voicing;
	/** Specifies whether voicing has been set. */
	private boolean hasVoicing = false;
	
	/**
	 * Constructs a general chord without specifying its pitch or voicing.
	 * Contains only a list of pitch classes comprising the chord.
	 * 
	 * @param base (Tone) The base pitch class of the chord.
	 * @param type (Chord.Chords) The type of the chord.
	 */
	public Chord(Tone base, Chords type) {
		this.size = type.size;
		tones = new Tone[size];
		tones[0] = base;
		for (int i=1; i<size; i++) {
			tones[i] = tones[i-1].up(type.intervals[i-1]);
		}
	}
	
	/**
	 * Constructs an exact chord with voicing specified by a known preset.
	 * The chord contains a list of the exact pitches as well as a general list of pitch classes.
	 * 
	 * @param base (Note) The pitch of the chord.
	 * @param type (Chord.Chords) The type of the chord.
	 */
	public Chord(Note base, Chords type) {
		this(base, 0, type.intervals);
	}
	/**
	 * Constructs an exact chord with voicing specified by a known preset.
	 * The chord contains a list of the exact pitches as well as a general list of pitch classes.
	 * 
	 * @param base (Note) The pitch of the chord.
	 * @param type (Chord.Chords) The type of the chord.
	 * @param inversion (int) Inversion of the chord
	 */
	public Chord(Note base, Chords type, int inversion) {
		this(base, inversion, type.intervals);
	}
	
	/**
	 * Constructs a custom Chord given a base note, an inversion and the building intervals.
	 * The chord has a specific voicing, the <b>tones</b> field is left empty.
	 * 
	 * @param base (Note) MIDI pitch of the base note.
	 * @param inversion (int) The inversion of the chord. If more than the length-1 is specified, this is ignored.
	 * @param structure (Interval[]) A sequence of intervals (going up) between the chord notes
	 * in the basic position (no inversion).
	 * 	Each interval specifies the next pitch <i>relative</i> to the previous.
	 */
	public Chord(Note base, int inversion, Interval... structure) {
		// The number of pitches in this chord
		this.size = structure.length + 1;
		// If the inversion is too big, ignore it
		if (inversion >= size) {inversion = 0;}
		// Prepare the array of Notes
		voicing = new Note[size];
		// Setup bass pitch
		voicing[0] = base;
		// Fill successive pitches
		for (int i=1; i<size; i++) {
			voicing[i] = voicing[i-1].up(structure[i-1]);
		}
		hasVoicing = true;
		// Make the inversion
		Note[] newVoicing = new Note[size];
		// Transpose by octave up and move first n notes; where n = inversion number
		for (int i=0; i<inversion; i++) {
			newVoicing[i+size-inversion] = voicing[i].up(C8);
		}
		// Move the rest of the notes
		for (int i=inversion; i<size; i++) {
			newVoicing[i-inversion] = voicing[i];
		}
		voicing = newVoicing;
	}
	
	
	
	/**
	 * List all notes comprising the chord as string. If no notes with exact pitches are given, list tones.
	 * 
	 * @return A list of pitches (if specified) or pitch classes (otherwise).
	 */
	public String list() {
		if (hasVoicing)	{return listNotes();}
		else			{return listTones();}
	}
	// Output tones into string
	private String listTones() {
		StringBuilder string = new StringBuilder();
		for (int i=0; i<tones.length-1; i++) {
			string.append(tones[i].name);
			string.append(", ");
		}
		string.append(tones[tones.length-1].name);
		return string.toString();
	}
	// Output notes into string
	private String listNotes() {
		StringBuilder string = new StringBuilder();
		for (int i=0; i<voicing.length-1; i++) {
			string.append(voicing[i].name());
			string.append(", ");
		}
		string.append(voicing[voicing.length-1].name());
		return string.toString();
	}
	
	public Note[] voicing() {
		return voicing;
	}
	
}
