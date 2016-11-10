package cz.slanyj.earTrainer;

import java.awt.Color;
import java.util.Random;

import javax.swing.SwingWorker;

import cz.slanyj.music.*;
import cz.slanyj.music.Key.Mode;
import static cz.slanyj.earTrainer.Trainer.Status.*;


/**
 * The core logic for the relative pitch trainer.
 * This object will create a simple random melody, play it using the "Sound" class
 * and wait for the user input (ie. answer) from the "GUI". Once an answer is obtained,
 * it is evaluated and a score is sent to ???where?
 * 
 * The procedure is as follows:
 * 1) Create new key (class "Key"): 
 * 		Choose a key (key base, ie. C, D, E... (enum "Tone"); and key type (or mode), ie. major, minor (class "Key").
 * 2) For the given key, play a cadence (method of the "Key" invoked from here).
 * 3) Choose a random sequence of notes from the set of allowed steps (method of the "Key", creates new "Melody").
 * 		The user will be able to change the set from which to choose (eg. only natural steps).
 * 4) Play the melody (method of "Melody").
 * 5) Display the key to the user (either by text or highlight a piano key).
 * 6) Set status to awaiting response.
 * 7) Get the note played by user and compare it to the first note of melody.
 * 		If correct, move to next note.
 * 		If wrong, wait for another input.
 * 8) Once all the melody is correct, start anew.
 * 
 * 
 */
class Trainer implements /*ActionListener,*/ NoteListener {
	
	/* FIELDS */
	/** The musical key of the excersize. */
	private XKey key;
	/** The task assigned to the user. */
	private MusicTask task;
	/** An object holding the preferences to be used by this trainer. */
	private Preferences preferences;	// Will be overridden anyway by setPreferences()
	/** Owning gui. */
	private GUI gui;
	
	/** A component able to pass XNote objects. */
	private NoteInput input;
	
	private Status status = IDLE;
	/** True when listening to user input note, false otherwise. */
	private boolean listening = false;
	/** The order of the excersize since the last change of key. */
	private int inThisKey = 0;
	/** Whether to start a next task automatically after finishing the previous. */
	private boolean auto = true;
	/** True if finish has been requested. */
	boolean excStopped = false;
	
//	/** Delay before key playback. */
//	private long keyDelay = 1500;
//	/** Delay before the playback of a new task in a new key. */
//	private long taskDelay = 500;
//	/** Delay before the playback of a new task in the same key. */
//	private long newTaskDelay = 1500;
	
	/** A random number generator. */
	private Random random = new Random();
		
	/* CONSTRUCTOR */
	Trainer() {
//		Master.getPreferences().selectKeyMode(Mode.MAJOR);
		preferences = Master.getPreferences();
		key = randomKey();
	}
	
	/** User preferences. */
	void setPreferences(Preferences preferences) {
//		Key.preferences = preferences;
	}
	/** Return the current preferences object. */
	Preferences getPreferences() {
		return preferences;
	}
	
	/** Reset preferences to default. */
	void resetPreferences() {
		// TODO�Copy code from user preferences
	}
	/** Load given preferences. */
	void loadPreferences(Preferences preferences) {
		this.preferences = preferences;
	}
		
	/* CORE CYCLE */
	/**
	 * Starts a new excersize by calling the key randomizer.
	 * @throws InterruptedException
	 */
	void start() throws InterruptedException {
		// Setup flag
		excStopped = false;
		// Generate new key
		newKey(0);
	}
	
	/**
	 * Sets a random key based on the preferences and displays its name.
	 * Once finished, proceeds to play the key.
	 * @param The delay of the next key playback.
	 */
	void newKey(long delay) {
		setStatus(ASSIGNING);
//		gui.display.clear();
		randomizeKey();
		// Reset counter
		inThisKey = 0;
		gui.displayInstructions(key.name());
		// Continue unless stop has been requested.
		if(!excStopped) playKey(delay);
	}
	
	/**
	 * Plays a specified cadence in order to establish the key.
	 * Once playback is finished, if the status is <code>ASSIGNING</code>,
	 * proceed to assign a new task to the user, otherwise continue waiting for user input.
	 * @param delay The delay of the key playback.
	 */
	void playKey(long delay) {
		Player p = new Player(delay) {
//			long pause = 500;
			@Override
			protected void play() throws InterruptedException {
				gui.display.clear();
				Sound.silence();
				key.playCadence().join();
//				gui.display.clear();
			}
			@Override
			protected void cont() {
				System.out.println("Key playback done");
				System.out.println(status);
				// If this is the first time playing this key (ie. has been invoked by the app, not the user)
				if (status == ASSIGNING) {
					// Create new task
					System.out.println("Creating new task");
					newTask(preferences.taskDelay);
				}	// Otherwise do nothing (= continue waiting for user input)
			}
		};
		// Run unless stop has been requested.
		if(!excStopped) p.execute();
//		gui.display.clear();
	}
	
	/**
	 * Creates a new task in the same key and increments the counter for this key.
	 * Once finished, proceeds to play the task.
	 */
	void newTask(long delay) {
		// Create new task
		task = new Melody(key, preferences);
		// Increment task counter
		inThisKey++;
		// Continue unless stop has been requested.
		if(!excStopped) playTask(delay);
	}
	
	/**
	 * Plays the task in a worker thread.
	 * Once finished, proceed to wait for the user reponse.
	 * 
	 */
	void playTask(long delay) {
		Player p = new Player(delay) {
			@Override
			protected void play() throws InterruptedException {
				gui.display.clear();
				// Present the task to the user
				task.play().join();
			}
			@Override
			protected void cont() {
				System.out.println("Task playback done");
				input.broadcast();
				setStatus(AWAITING_RESPONSE);
			}
		};
		// Run unless stop has been requested.
		if(!excStopped) p.execute();
	}
	
	/**
	 * Invoked from <code>input</code>.
	 * Process the received input.
	 */
	public void listen(Note note) throws InterruptedException {
		if (status == AWAITING_RESPONSE && !excStopped)
			checkNote(note);
	}
	
	/**
	 * Checks this note against the task. If this note completes the task,
	 * @param note
	 * @throws InterruptedException
	 */
	private void checkNote(Note note) throws InterruptedException {
		System.out.println(note.name());
		int done = task.check(note);
		if (done==0) {
			// The answer is wrong
			gui.display.wrong();
		} else if (done==1) {
			// The answer is not complete yet
			gui.display.goodSoFar();
		} else {
			input.stopBroadcast();
			gui.display.correct();
			System.out.println("Correct!");
//			Thread.sleep(1000);		// Don't call sleep() on the Event Dispatch Thread!
			if (auto) nextTask();
		}
		
	}
	
	/**
	 * Decides whether key change is appropriate and starts a new task.
	 */
	void nextTask() {
		// Check how many tasks have been run in this key and compare it to the user setting
		if ((inThisKey<preferences.tasksPerKey || preferences.tasksPerKey==0) && !excStopped) {
			// Continue in the same key
			newTask(preferences.newTaskDelay);
		} else if (!excStopped) {
			// Change key
			newKey(preferences.keyDelay);
		}
	}
	
	
	void test() {
//		key = randomKey();
//		System.out.println(key.name());
//		new KeyPlayer().execute();
//		gui.displayInstructions(key.name());
		
/*		Player exc = new MelodyExc(key);
		exc.execute();*/
	}
	
	/** Stop the excersize. */
	void stopExc() {
		excStopped = true;
		gui.display.clear();
		Sound.stop();
		input.setActive();
	}
	
	/** Bind a GUI to this frame */
	synchronized void associate(GUI gui) {
		this.gui = gui;
		bindNoteInput();
//		this.input = gui.getNoteInput();
//		input.setTarget(this);
	}
	/** Binds the current NoteInput from the GUI to this trainer. */
	synchronized void bindNoteInput() {
		this.input = gui.getNoteInput();
		input.setTarget(this);
	}

	/** Pretty self-explanatory, isn't it? */
	private void randomizeKey() {
		key = randomKey();
	}
		
	/* PRIVATE METHODS */
	/** Returns a random XKey chosen from allowed keys as set in preferences. */
	private XKey randomKey() {
		Key[] allowed = preferences.keysAllowed();
		int rdm = random.nextInt(allowed.length);
		return new XKey(allowed[rdm]);
	}
	
	private void setStatus(Status status) {
		this.status = status;
	}
	
	

	
	/** A list of possible states for the trainer. */
	enum Status {
		IDLE,
		ASSIGNING,
		AWAITING_RESPONSE;
	}
	
	/**
	 * A player to play MIDI sequence in a separate thread.
	 * Instantiate this in the Event Dispatch Thread for each sequence played.
	 * <p>
	 * This is a generic player class which needs to be subclassed to
	 * define a concrete type of player in order to differentiate
	 * the point the program is currently at (needed to provide proper continuing mehtods).
	 * The subclass will provide implementation for the following methods:
	 * <li><b>play()</b> Code to play the desired music (key, task...). Invoked at the start of the worker thread.</li>
	 * <li><b>cont()</b> What to do once the playback is finished. Invoked once the MIDI sequence terminates.</li>
	 */
	private abstract class Player extends SwingWorker<Void, String> {
		
		/** The time (in milliseconds) to wait before starting the playback. */
		protected long pause;
		
		/** Constructs a new Player with the pause period specified. */
		protected Player(long pause) {
			this.pause = pause;
		}
		/** Constructs a new Player with the default value of pause = 0. */ 
		protected Player() {
			this(0);
		}
		
		/**
		 * In another thread, create a new random melody and play it.
		 * @return Nothing (a Void object).
		 */
		protected Void doInBackground() {
			try {
				// Disable input
				input.setInactive();
				input.stopBroadcast();
				// Wait
				Thread.sleep(pause);
				// Play it
				play();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;	// A method declared Void must return a null object
		}
		
		protected void done() {
			// Activate input
			input.setActive();
//			input.broadcast();
			System.out.println("Player done");
			cont();
		}
		
		/**
		 * Plays the element to the user. The note input is disabled for the duration of playback.
		 * (Sets <code>input</code> to <code>inactive</code> and makes it stop broadcasting keys.
		 */
		protected abstract void play() throws InterruptedException;
		/** What to do when the playback finishes. */
		protected abstract void cont();

	}

}
