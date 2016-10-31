package cz.slanyj.music;

import static cz.slanyj.music.Interval.*;
import static cz.slanyj.music.Tone.*;

/**
 * This enum represents musical keys.
 * 
 * @author Sorondil
 *
 */


public enum Key_old {
	// Major
	CES_MAJ (CES, Type.MAJOR),
	GES_MAJ (GES, Type.MAJOR),
	DES_MAJ (DES, Type.MAJOR),
	AS_MAJ (AS, Type.MAJOR),
	ES_MAJ (ES, Type.MAJOR),
	BB_MAJ (BB, Type.MAJOR),
	F_MAJ (F, Type.MAJOR),
	C_MAJ (C, Type.MAJOR),
	G_MAJ (G, Type.MAJOR),
	D_MAJ (D, Type.MAJOR),
	A_MAJ (A, Type.MAJOR),
	E_MAJ (E, Type.MAJOR),
	H_MAJ (H, Type.MAJOR),
	FIS_MAJ (FIS, Type.MAJOR),
	CIS_MAJ (CIS, Type.MAJOR),
	// Minor
	AS_MIN (AS, Type.MINOR),
	ES_MIN (ES, Type.MINOR),
	BB_MIN (BB, Type.MINOR),
	F_MIN (F, Type.MINOR),
	C_MIN (C, Type.MINOR),
	G_MIN (G, Type.MINOR),
	D_MIN (D, Type.MINOR),
	A_MIN (A, Type.MINOR),
	E_MIN (E, Type.MINOR),
	H_MIN (H, Type.MINOR),
	FIS_MIN (FIS, Type.MINOR),
	CIS_MIN (CIS, Type.MINOR),
	GIS_MIN (GIS, Type.MINOR),
	DIS_MIN (DIS, Type.MINOR),
	AIS_MIN (AIS, Type.MINOR);
	
	// Need this in separate enum to make it initialize when initializing Key
	public static enum Type {
		// Express the scale in terms of intervals up from the base
		MAJOR ("major", C1, V2, V3, C4, C5, V6, V7),
		MINOR ("minor", C1, V2, M3, C4, C5, M6, M7);
		
		Interval[] steps;
		String tonality;
		
		Type(String tonality, Interval... intervals) {
			this.steps = intervals;
			this.tonality = tonality;
		}
	}

	/* FIELDS */
	public final Tone base;
	public final Interval[] type;
	public Tone[] scale;
	public final String tonality;
	
	/* CONSTRUCTORS AND�INITIALIZERS */
	static {
		for (Key_old key : values()) {
			key.scale();
		}
	}
	
	Key_old(Tone base, Type type) {
		this.base = base;
		this.type = type.steps;
		this.tonality = type.tonality;
	}
	
	/* METHODS */
	public static Key_old[] getAllKeys() {
		return Key_old.values();
	}
	
	/* PRIVATE METHODS */

	private void scale() {
		// Initialize an array to hold the scale tones
		scale = new Tone[type.length];
		// Set the base
		scale[0] = base;
//		System.out.println("Adding "+scale[0].name+" to scale.");
		// Fill subsequent steps
		for (int i=1; i<type.length; i++) {
			scale[i] = base.up(type[i]);
//			System.out.println("Adding "+scale[i].name+" to scale.");
		}
		
	}
	
}
