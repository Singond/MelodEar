package cz.slanyj.earTrainer;

import cz.slanyj.music.Note;

interface NoteListener {
	
	public void listen(Note note) throws InterruptedException;
	
}
