package cz.slanyj.earTrainer;

public class Master {

	/* FIELDS */
	// Default settings
	static Preferences preferences = new Preferences();
	static GUI gui;
	static Trainer trainer;
	
	
	/* GETTERS AND�SETTERS */
	static Preferences getPreferences() {
		return preferences;
	}
	static void bind(Trainer trn, GUI gui) {
		gui.associate(trn);
		trn.associate(gui);
	}
	
	
	
	public static void main(String[] args) {
		// Create the GUI
		gui = new GUI();
		trainer = new Trainer();
		gui.start();	// A new thread is created here (method run() in GUI).
		bind(trainer, gui);
		trainer.test();

	}

}
