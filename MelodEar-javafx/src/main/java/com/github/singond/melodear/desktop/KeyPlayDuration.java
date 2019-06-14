package com.github.singond.melodear.desktop;

/**
 * Specifies how long a keyboard key produces sound after being clicked.
 * <p>
 * More precisely, this specifies when the Note Off event occurs after
 * pressing a key. Note that a Note Off event may not be the only way
 * to terminate a note. For example, some devices support may terminate
 * the note by fading out.
 *
 * @author Singon
 */
public enum KeyPlayDuration {

	/** The note sounds as long as its key is depressed. */
	KEY_HELD,
	/** The note sounds until another key is pressed. */
	NEXT_KEY,
	/** The note sounds indefinitely long. */
	INDEFINITE;

}
