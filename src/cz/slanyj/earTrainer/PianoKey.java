package cz.slanyj.earTrainer;

enum PianoKey {
	//		ptch	pos.	isWhite
	C		(0,		0,		true),
		CIS	(1,		15,		false),
	D		(2,		24,		true),
		DIS	(3,		43,		false),
	E		(4,		48,		true),
	F		(5,		72,		true),
		FIS	(6,		85, 	false),
	G		(7,		96,		true),
		GIS	(8,		113,	false),
	A		(9,		120,	true),
		BB	(10,	141,	false),
	H		(11,	144,	true);		// Using German notation internally, cause I'm no Englishman
	
	/** The pitch class. */
	final int pitchClass;
	/** The offset of the left edge of the key from the left edge of the C key on piano. */
	final int keyBoardPosition;
	/** Specifies whether the key is white. If true, it is a white key, if false, it is a black key. */
	final boolean isWhite;
	
	// CONSTRUCTOR
	private PianoKey(int pitchClass, int position, boolean isWhite) {
		this.pitchClass = pitchClass;
		this.keyBoardPosition = position;
		this.isWhite = isWhite;
	}
	/** Find a piano key corresponding to a given pitch class. */
	static PianoKey find(int pitchClass) throws IllegalArgumentException {
		for(PianoKey key : values()) {
			if (key.pitchClass == pitchClass) {
				return key;
			} else {
				continue;
			}
		}
		throw new IllegalArgumentException("Could not find correct pitch class: Variable int 'step' is too big.");
	}
}
