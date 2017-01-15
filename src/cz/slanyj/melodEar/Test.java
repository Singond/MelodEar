package cz.slanyj.melodEar;

import cz.slanyj.music.*;
import cz.slanyj.music.Chord.Chords;
import static cz.slanyj.music.Interval.*;
import static cz.slanyj.music.Tone.*;

class Test {
	
	public static void main(String[] args) {
		
/*		// Keep this, throws an error
		// TODO�Fix this!
		Note note = new Note(GIS, 4);
		Chord chord = new Chord(note, Chords.MAJ);
		System.out.println(chord.list());*/
		
//		Tone note = Tone.GIS;
//		System.out.println(note.up(V3));
		
//		System.out.println(-11%12);
		
		Tone tone = Tone.find(11, H);
		System.out.println(tone.name);
		
/*		Note note = new Note(GIS, 4);
		note = note.up(M3);
		System.out.println(note.name());*/
	}
}