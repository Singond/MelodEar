package cz.slanyj.melodEar;
/**
 * Displays a clickable piano keyboard, playing sounds when a key is clicked.
 * The section to be displayed is specified in the constructor.
 * <p>
 * The keyboard is an extension of JLayeredPane, where the white keys
 * are grouped in one layer and the black keys in another on top of the first.
 * Absolute positioning is used to both align the layers
 * and position the black keys within their layer.
 * The keys themselves are an extension of JButton.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import cz.slanyj.music.Note;

@SuppressWarnings("serial")
public class Keyboard extends JLayeredPane implements ActionListener, NoteInput {
	
	/** A button representing a single piano key. Has subclasses for a white key and a black key.<p>
	 * The keys are positioned on the keyboard as specified in PianoKey.
	 * @author Sorondil
	 */
	private class Key extends JButton {
		//
		/* Constructors */
		private Key(Note note) {
			// Setup fields
			this.note = note;
			PianoKey key = PianoKey.find(note.tone.pitchClass);
			position = (int)Math.round(key.keyBoardPosition*unit + octaveSpan*note.octave - lowEnd);
			// Add action listener
			this.addActionListener(Keyboard.this);		// Keyboard.this refers to the parent (instance of Keyboard)
		}

		private final Note note;
		int position;

		/** Plays the associated note. */
		private void strike() {
			Sound.play(note);
		}
	}
	/** A white piano key. */
	private class WhiteKey extends Key {
		private WhiteKey(Note note) {
			super(note);
			this.setBackground(Color.WHITE);
			this.setBounds(position, 0, whiteWidth, whiteHeight);
			whites.add(this);
		}
	}
	/** A black piano key. */
	private class BlackKey extends Key {
		private BlackKey(Note note) {
			super(note);
			this.setBackground(Color.BLACK);
			this.setBounds(position, 0, blackWidth, blackHeight);
			blacks.add(this);
		}
	}
	

	// Key dimensions (Borders are drawn on the inside)
	/** The unit of key dimensions. Scale this to scale the keyboard. */
	private double unit = 2;
	private final int whiteWidth = (int)Math.round(24*unit);
	private final int whiteHeight = (int)Math.round(155*unit);
	private final int blackWidth = (int)Math.round(14*unit);
	private final int blackHeight = (int)Math.round(103*unit);
	/** The display width of one octave. */
	private final int octaveSpan = 7*whiteWidth;
	/** Horizontal coordinate of the lowest key (left edge). */
	int lowEnd = 0;
	
	/** A panel with white keys. */
	JPanel whites;
	/** A panel with black keys. */
	JPanel blacks;
	
	NoteListener noteListener;
	
	/** True if the keyboard responds to key clicks. */
	private boolean active = true;
	/** True if another object is listening for the Note output. */
	private boolean broadcasting = false;
	
	/**
	 * Constructs a new Keyboard object displaying the specified section of a keyboard.
	 * @param startPitch The MIDI pitch of the lowest note to be displayed.
	 * @param endPitch The MIDI pitch of the highest note to be displayed.
	 */
	public Keyboard(int startPitch, int endPitch) {
		draw(startPitch, endPitch);
	}
	
	// From NoteInput
	/** Disable the keyboard, ie. make it stop responding to input. */
	public void setInactive() {
		active = false;
	}
	/** Enable the keyboard, ie. make it respond to user input. */
	public void setActive() {
		active = true;
	}
	/** Make the keyboard broadcast its Notes pressed. */
	public void broadcast() {
		broadcasting = true;
	}
	/** Stop broadcasting the Notes. */
	public void stopBroadcast() {
		broadcasting = false;
	}
	/** Set the NoteListener listening for input from this NoteInput. */
	public void setTarget(NoteListener o) {
		noteListener = o;
	}
	
	// From ActionListener
	/** Process a click. */
	public void actionPerformed(ActionEvent e) {
		if (active) {
			Key clicked = (Key)(e.getSource());
			clicked.strike();
			if (broadcasting) {
				try {
					noteListener.listen(clicked.note);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void redraw(int startPitch, int endPitch) {
		remove(whites);
		remove(blacks);
		draw(startPitch, endPitch);
		revalidate();
		repaint();
	}
	
	/**
	 * Creates a key and adds it to the keyboard.
	 * The distinction between black and white keys is specified in PianoKey.isWhite.
	 * 
	 * @param pitch (int) The pitch of the note to be added.
	 */
	private void addKey(int pitch) {
		Note newNote = new Note(pitch);
		if (PianoKey.find(newNote.tone.pitchClass).isWhite) {
			new WhiteKey(newNote);
		} else {
			new BlackKey(newNote);
		}
	}
	
	/**
	 * Adds all keys in the specified range to the keyboard.
	 */
	private void fillKeys(int startPitch, int endPitch) {
		for (int i=0; i<=(endPitch-startPitch); i++) {
			addKey(startPitch+i);
		}
	}
	
	/**
	 * 
	 * @param startPitch
	 * @param endPitch
	 */
	private void draw(int startPitch, int endPitch) {
		// Not really adding any key, just calculating its position on keyboard
		// lowEnd must be zero!
		lowEnd = 0;
		lowEnd = new Key(new Note(startPitch)).position;
		
		// Create the layers, set their layout and place them into the panel.
		whites = new JPanel();
		blacks = new JPanel();
		whites.setLayout(null);
		blacks.setLayout(null);
		add(whites, new Integer(0));	// Layer 0 is the lower one
		add(blacks, new Integer(1));	// Black keys are displayed over white
		
		// Calculate the size of the panels from the extent of the white keys
		int width = new Key(new Note(endPitch)).position+whiteWidth;
		// Set preferred size manually, the setBounds method may not work as expected
		whites.setPreferredSize(new Dimension(width, whiteHeight));
		whites.setBounds(0, 0, width, whiteHeight);
		blacks.setBounds(0, 0, width, blackHeight);
		
		// Display the layers
		whites.setOpaque(false);
		blacks.setOpaque(false);
		
		// Add all keys to their specific layers
		fillKeys(startPitch, endPitch);
		
		// Inherit the preferred size from the white keys panel,
		// otherwise the size would be zero.
		setPreferredSize(whites.getPreferredSize());
	}
	
}
