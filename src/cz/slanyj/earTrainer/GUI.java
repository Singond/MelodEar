package cz.slanyj.earTrainer;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import static javax.swing.SwingConstants.*;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Component;

import javax.swing.border.TitledBorder;

import cz.slanyj.swing.*;

class GUI implements ActionListener {
	
	/** Owning instance of trainer. */
	private Trainer trainer;
	/** The preferences of the trainer. */
	private Preferences prefs;
	
	JFrame window;
	Container pane;
	Controls controls;	// JPanel
	Keyboard keyboard;
	JLabel instructions;
	Display display = new Display();
	private JPanel panel;
	private JMenuBar menuBar;
	private JPanel kbdSettings;
	private JPanel internals;
	private IntSpinner keyDelaySpn;
	private Component rigidArea_1;
	private IntSpinner taskDelaySpn;
	private Component rigidArea_2;
	private IntSpinner newTaskDelaySpn;
	
	/** Lowest key to display on the keyboard. */
	private int kbdLo = 53;
	/** Highest key to display on the keyboard. */
	private int kbdHi = 84;
	private JPanel excersize;
	private IntSpinner melLengthSpn;
	private Component rigidArea;
	private Component rigidArea_3;
	private Component rigidArea_4;
	private Component rigidArea_5;
	private Component rigidArea_6;
	private IntSpinner perKeySpn;
	private IntSpinner melFromSpn;
	private IntSpinner melToSpn;
	private IntSpinner octavesSpn;
	private IntSpinner startOctSpn;
	private JPanel kbdContainer;
	private JScrollPane scrollPane;
	private Component horizontalGlue;
	
	/**
	 * Creates a new GUI and populates it with its contents
	 */
	GUI() {
		// Create a frame
		window = new JFrame("Ear Trainer");
		window.getContentPane().setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		window.setIconImage();
		
		// Set up content pane
		pane = window.getContentPane();
				
				panel = new JPanel();
				panel.setBorder(null);
				GridBagLayout gbl_panel = new GridBagLayout();
				gbl_panel.rowHeights = new int[]{0, 150, 0, 24};
				gbl_panel.columnWidths = new int[]{58, 0, 0, 0};
				gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0};
				gbl_panel.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0};
				panel.setLayout(gbl_panel);
				
						// Create the text display area
						GridBagConstraints gbc_display = new GridBagConstraints();
						gbc_display.fill = GridBagConstraints.BOTH;
						gbc_display.gridwidth = 4;
						gbc_display.insets = new Insets(0, 0, 5, 0);
						gbc_display.gridx = 0;
						gbc_display.gridy = 0;
						display.setMinimumSize(new Dimension(0, 32));
						display.setPreferredSize(new Dimension(0, 32));
						panel.add(display, gbc_display);
						
						// Create control panel
						controls = new Controls();
						controls.setBorder(null);
						GridBagConstraints gbc_controls = new GridBagConstraints();
						gbc_controls.fill = GridBagConstraints.VERTICAL;
						gbc_controls.gridheight = 2;
						gbc_controls.insets = new Insets(0, 0, 5, 5);
						gbc_controls.gridx = 0;
						gbc_controls.gridy = 1;
						panel.add(controls, gbc_controls);
						
						internals = new JPanel();
						internals.setBorder(new TitledBorder(null, "Internal", TitledBorder.LEADING, TitledBorder.TOP, null, null));
						GridBagConstraints gbc_internals = new GridBagConstraints();
						gbc_internals.anchor = GridBagConstraints.NORTH;
						gbc_internals.insets = new Insets(0, 0, 5, 5);
						gbc_internals.fill = GridBagConstraints.HORIZONTAL;
						gbc_internals.gridx = 2;
						gbc_internals.gridy = 1;
						panel.add(internals, gbc_internals);
						internals.setLayout(new BoxLayout(internals, BoxLayout.Y_AXIS));
						
							keyDelaySpn = new IntSpinner((int)Preferences.keyDelayDft, 0, 2500, 100, "Key delay",
								n -> prefs.keyDelay=(long)(n)
							);
							keyDelaySpn.setToolTipText("The delay (in milliseconds) before the playback of a new key.");
							internals.add(keyDelaySpn);
							
							rigidArea_1 = Box.createRigidArea(new Dimension(0, 4));
							internals.add(rigidArea_1);
							
							taskDelaySpn = new IntSpinner((int)Preferences.taskDelayDft, 0, 2500, 100, "Task delay",
								n -> prefs.taskDelay=(long)(n)
							);
							taskDelaySpn.setToolTipText("The delay (in milliseconds) between the playback of a key and a new task in that key.");
							internals.add(taskDelaySpn);
							
							rigidArea_2 = Box.createRigidArea(new Dimension(0, 4));
							internals.add(rigidArea_2);
							
							newTaskDelaySpn = new IntSpinner((int)Preferences.newTaskDelayDft, 0, 2500, 100, "New task delay",
								n -> prefs.newTaskDelay=(long)(n)
							);
							newTaskDelaySpn.setToolTipText("The delay (in milliseconds) before the playback of a new task in the same key.");
							internals.add(newTaskDelaySpn);
						
						excersize = new JPanel();
						excersize.setBorder(new TitledBorder(null, "Excersize", TitledBorder.LEADING, TitledBorder.TOP, null, null));
						GridBagConstraints gbc_excersize = new GridBagConstraints();
						gbc_excersize.gridheight = 2;
						gbc_excersize.insets = new Insets(0, 0, 5, 0);
						gbc_excersize.fill = GridBagConstraints.BOTH;
						gbc_excersize.gridx = 3;
						gbc_excersize.gridy = 1;
						panel.add(excersize, gbc_excersize);
						excersize.setLayout(new BoxLayout(excersize, BoxLayout.Y_AXIS));
						
							// Excersize settings
							melLengthSpn = new IntSpinner(1, 1, 10, 1, "Melody length", n -> prefs.melodyLength=n);
							melLengthSpn.setToolTipText("Number of notes in the melody");
							excersize.add(melLengthSpn);
							
							rigidArea = Box.createRigidArea(new Dimension(0, 4));
							excersize.add(rigidArea);
							
							perKeySpn = new IntSpinner(1, 0, 10, 1, "Tasks per key", n -> prefs.tasksPerKey=n);
							perKeySpn.setToolTipText("The number of tasks to perform before the key is changed again. Set this to 0 to make the key constant.");
							excersize.add(perKeySpn);
							
							rigidArea_3 = Box.createRigidArea(new Dimension(0, 4));
							excersize.add(rigidArea_3);
							
							melFromSpn = new IntSpinner(1, 1, 7, 1, "Lower melody limit", n -> prefs.melodySpan[0]=n);
							excersize.add(melFromSpn);
							
							rigidArea_4 = Box.createRigidArea(new Dimension(0, 4));
							excersize.add(rigidArea_4);
							
							melToSpn = new IntSpinner(1, 1, 7, 1, "Upper melody limit", n -> prefs.melodySpan[1]=n);
							excersize.add(melToSpn);
							
							rigidArea_5 = Box.createRigidArea(new Dimension(0, 4));
							excersize.add(rigidArea_5);
							
							startOctSpn = new IntSpinner(4, 1, 10, 1, "Start in octave", n -> prefs.melodySpan[2]=n);
							excersize.add(startOctSpn);
							
							rigidArea_6 = Box.createRigidArea(new Dimension(0, 4));
							excersize.add(rigidArea_6);
							
							octavesSpn = new IntSpinner(1, 1, 10, 1, "Octave difference", n -> prefs.melodySpan[3]=n);
							excersize.add(octavesSpn);
						
						kbdSettings = new JPanel();
						kbdSettings.setBorder(new TitledBorder(null, "Keyboard", TitledBorder.LEADING, TitledBorder.TOP, null, null));
						GridBagConstraints gbc_kbdSettings = new GridBagConstraints();
						gbc_kbdSettings.insets = new Insets(0, 0, 5, 5);
						gbc_kbdSettings.fill = GridBagConstraints.BOTH;
						gbc_kbdSettings.gridx = 2;
						gbc_kbdSettings.gridy = 2;
						panel.add(kbdSettings, gbc_kbdSettings);
						kbdSettings.setLayout(new BoxLayout(kbdSettings, BoxLayout.Y_AXIS));
						
							IntSpinner intSpinner = new IntSpinner(kbdLo, 21, 108, 1, "Lowest key",
								n -> {
									kbdLo=n;
									keyboard.redraw(kbdLo, kbdHi);
							});
							intSpinner.setToolTipText("The lowest key to be displayed on the keyboard.");
							kbdSettings.add(intSpinner);
							
							kbdSettings.add(Box.createRigidArea(new Dimension(0, 4)));
							
							IntSpinner intSpinner_1 = new IntSpinner(kbdHi, 21, 108, 1, "Highest key",
								n -> {
									kbdHi=n;
									keyboard.redraw(kbdLo, kbdHi);
							});
							intSpinner_1.setToolTipText("The highest key to be displayed on the keyboard.");
							kbdSettings.add(intSpinner_1);
				
					
						// Create text panel
						instructions = new JLabel();
						GridBagConstraints gbc_instructions = new GridBagConstraints();
						gbc_instructions.gridwidth = 4;
						gbc_instructions.gridx = 0;
						gbc_instructions.gridy = 3;
						panel.add(instructions, gbc_instructions);
						instructions.setPreferredSize(new Dimension(400, 50));
						instructions.setHorizontalAlignment(CENTER);
						
						// Create keyboard panel
						paintKeyboard();
				
				menuBar = new JMenuBar();
				window.setJMenuBar(menuBar);
				
				JMenu mnMenu = new JMenu("Menu");
				menuBar.add(mnMenu);
				
				JMenu mnOptions = new JMenu("Options");
				menuBar.add(mnOptions);
	}
	/**
	 * Paints a new keyboard with keys currently set as the limits.
	 * The limits are the private fields <code>kbdLo</code> and <code>kbdHi</code>
	 * of this GUI object.
	 */
	private void paintKeyboard() {
		pane.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
		pane.add(panel);
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		window.getContentPane().add(scrollPane);
		
		kbdContainer = new JPanel();
		kbdContainer.setAlignmentY(Component.TOP_ALIGNMENT);
		scrollPane.setViewportView(kbdContainer);
		kbdContainer.setLayout(new BoxLayout(kbdContainer, BoxLayout.X_AXIS));
		
		horizontalGlue = Box.createHorizontalGlue();
		kbdContainer.add(horizontalGlue);
		keyboard = new Keyboard(kbdLo, kbdHi);
		kbdContainer.add(keyboard);
	}
	
	private void createGUI() {
		// Get the icon and set it
		ImageIcon icon = new ImageIcon(GUI.class.getResource("/img/clef128.png"));
//		ImageIcon icon = new ImageIcon("/img/clef128.png");
		Image img = icon.getImage();
		window.setIconImage(img);
//		label.setText("Text");
//		label.setIcon(icon);
		
		
		// Display it all
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	/** Returns the component from which Notes are received on user input. */
	NoteInput getNoteInput() {
		return (NoteInput)keyboard;
	}
	
	void associate(Trainer trainer) {
		this.trainer = trainer;
		this.prefs = trainer.getPreferences();
		controls.setTrainer(trainer);
//		settings.setTarget(trainer.getPreferences());
	}
	
	void displayInstructions(String text) {
		instructions.setText(text);
	}
	
	
	@Override
	// Action Listener
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	// START
	void start() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
			
		});
	}
}
