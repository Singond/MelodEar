package cz.slanyj.earTrainer;

interface NoteInput {

	/** Disable the keyboard, ie. make it stop responding to input. */
	void setInactive();
	
	/** Enable the keyboard, ie. make it respond to user input. */
	void setActive();
	
	/** Make the keyboard broadcast its Notes pressed. */
	public void broadcast();
	
	/** Stop broadcasting the Notes. */
	public void stopBroadcast();
	
	/** Set the object to receive the note input. */
	void setTarget(NoteListener o);
	
}
