package cz.slanyj.melodEar;

import javax.sound.midi.*;

import static javax.sound.midi.ShortMessage.*;
import static javax.sound.midi.MetaMessage.*;
import cz.slanyj.music.*;

/**
 * A static class used to produce sound.
 * XNotes, XKeys and XChords use this class in their <strong>play</strong> methods.
 * @author Sorondil
 *
 */
class Sound {
	
	private static Receiver receiver;
	private static Sequencer seq;
	
	/** A listener of MIDI meta-events used to end playback when the end of track is reached. */
	private static EndListener listener;
	
	/** The MIDI-sequence currently prepared for playback */
	private static Sequence preparedSequence;
	
	// Default MIDI values to be used by other classes when constructing sequences for Sound.play()
	/** The MIDI channel used to play the notes. */
	static int channel = 0;
	/** MIDI velocity of the notes to be played. */
	static int velocity = 93;
	/** The tempo in BPM. */
	static int bpm = 120;
	/** MIDI resolution (number of ticks per quarter note). */
	static int resolution = 48;
	/** Is playing. */
	private static boolean playing = false;
	/** A thread lock. */
	private static final Object lock = new Object();
	/** A thread responsible for playing the sound. */
	static Thread player;
	
	
	static {
		try {
			receiver = MidiSystem.getReceiver();
			seq = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			// TODO Write exception for unavailable MIDI
		}
		listener = new EndListener();
	}
	
	/* Individual notes */
	
	/**
	 * Plays a note directly without creating a MIDI sequence.<p>
	 * Sends a Note On MIDI message to the receiver. Note Off is not specified.
	 * 
	 * @param note (Note) The note to be played.
	 */
	static void play(Note note) {
		int channel = 0;
		int velocity = 93;
		try {
			receiver.send(new ShortMessage(NOTE_ON, channel, note.pitch, velocity), -1);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Silences all sounding notes.
	 */
	static void silence() {
		// 0xB0 0x78 0x00: All sound off (add channel number to first byte)
		try {
			receiver.send(new ShortMessage(CONTROL_CHANGE, 0x78, 0), -1);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	/* Sequences */
	
	/**
	 * Prepare playback for a given MIDI-sequence.
	 * This method should do all tasks which can be performed in advance,
	 * in order to speed up the start of the subsequent playback.
	 */
	static void prepare(Sequence sequence) {
		try {
			// Open sequencer
			seq.open();
			seq.setTempoInBPM(bpm);
			// Add listener to close the sequencer on end of track
			seq.addMetaEventListener(listener);
			// Load the sequence into the sequencer
			seq.setSequence(sequence);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		preparedSequence = sequence;
	}
	
	/**
	 * 
	 * @param sequence
	 * @return
	 * @throws IllegalStateException if the sequencer is closed
	 */
	static synchronized Thread play(Sequence sequence) {
		Thread playingThread;
		if (preparedSequence == sequence) {
			playingThread = playPreparedInThread();
		} else {
			prepare(sequence);
			playingThread = playPreparedInThread();
		}
		return playingThread;
	}
	
	/**
	 * Plays the prepared MIDI sequence using the default MIDI device.<p>
	 * @param sequence (Sequence) The sequence to be played.
	 * @throws IllegalStateException if the sequencer is closed
	 */
	private static void playPrepared() {
		playing = true;
		seq.start();
	}
	
	/* Sequences */
	
	/**
	 * Play the prepared given MIDI sequence in a new thread. This thread is a wrapper one,
	 * because the MIDI sequencer's inner thread (if any) is apparently inaccessible.
	 * The wrapper thread terminates when the end of the sequence is reached
	 * (MIDI meta message "End of Track" is read).
	 * 
	 * @param sequence The MIDI sequence to be played.
	 * @return The wrapper thread.
	 * @throws IllegalStateException if the sequencer is closed
	 */
	private static Thread playPreparedInThread() {
		playing = true;
		// Create the wrapper thread
		player = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (listener) {
					playPrepared();
					// While playing, keep this thread alive (using listener as the lock)
					while (playing) {
						try {
							listener.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} finally {
							// Always close the sequencer!
							stop();
						}
					}
				}
			}
		});
		player.start();
		return player;		
	}
	/**
	 * Stop playing whatever sequence is being played now.
	 * This only terminates note passed by sequence,
	 * not notes started directly.
	 */
	static void stop() {
		seq.close();
		System.out.println("Sequencer closed.");
	}
	
	private static class EndListener implements MetaEventListener {
		@Override
		public void meta(MetaMessage meta) {
			if (meta.getType() == 0x2F) {
				notifyLock();
			}
		}
		
		private synchronized void notifyLock() {
			playing = false;
			notifyAll();
		}
	}
	
}
