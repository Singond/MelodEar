package cz.slanyj.earTrainer;

interface MusicTask {

	/**
	 * Plays this excersize in a new thread.
	 * This thread terminates only when the sequence is finished playing.
	 * This can be used to pause execution until the sequence finishes.
	 * @return The thread in which the sequence is played.
	 * 
	 */
	Thread play();
	/**
	 * Checks a Note object from user input and compares it to the melody.<p>
	 * The note to be checked is compared to the
	 * note under the cursor. If the notes do not match, return 0 and reset cursor to zero.
	 * If the notes match, move the cursor to the next note and return 1 if more notes follow
	 * or return 2 if it was the last note in melody.
	 * @param note The note to be checked.
	 * @return Integer representing one of the possibilities:
	 * <ul>
	 * <li><b>0</b> The notes do not match.</li>
	 * <li><b>1</b> The notes match, but more notes follow in the task.</li>
	 * <li><b>2</b> The notes match and no more notes follow, ie. the task is complete.</li>
	 * </ul>
	 */
	int check(cz.slanyj.music.Note note);
}
