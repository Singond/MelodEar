package com.github.singond.melodear;

import com.github.singond.music.Pitch;

public interface PitchParser {

	/**
	 * Returns a Pitch represented by the given character sequence.
	 * The format of the text understood by the parser is dependent
	 * on the implementation.
	 *
	 * @param seq the character sequence representing a pitch
	 * @return the pitch represented by {@code seq}
	 */
	Pitch parse(CharSequence seq);
}
