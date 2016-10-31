package cz.slanyj.earTrainer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cz.slanyj.swing.IntSpinner;
import java.awt.Component;

public class Settings extends JPanel {
	
	private Preferences target;
	
	int a = 1;
	int b = 10;
	int c = 100;
	
/*	IntField mel;
	IntField ant;
	IntField yet;*/
	
	Settings() {
	
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		setPreferredSize(new Dimension(200, 300));
		
/*		mel = new NumField(1, 1, 10, 1, "Melody length", n -> System.out.println("Mel accepted "+n));
		add(mel);
		ant = new NumField(1, 1, 10, 1, "Another value", n -> System.out.println("Ant accepted "+n));
		add(ant);
		yet = new NumField(1, 1, 10, 1, "Yet another", n -> System.out.println("Yet accepted "+n));
		add(yet);*/
		add(new IntSpinner(1, 1, 10, 1, "Melody length", n -> target.melodyLength=n ));
		
		Component rigidArea = Box.createRigidArea(new Dimension(0, 4));
		add(rigidArea);
		add(new IntSpinner(1, 0, 10, 1, "Tasks per key", n -> target.tasksPerKey=n ));
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(0, 4));
		add(rigidArea_1);
//		add(new NumField(1, 1, 10, 1, "Yet another", n -> System.out.println("Yet accepted "+n) ));
//		JCheckBox constantKeyBut = new JCheckBox("Retain key between excersizes");
//		add(constantKeyBut);
		add(new IntSpinner(1, 1, 7, 1, "Lower melody limit", n -> target.melodySpan[0]=n ));
		
		Component rigidArea_2 = Box.createRigidArea(new Dimension(0, 4));
		add(rigidArea_2);
		add(new IntSpinner(1, 1, 7, 1, "Upper melody limit", n -> target.melodySpan[1]=n ));
		
		Component rigidArea_3 = Box.createRigidArea(new Dimension(0, 4));
		add(rigidArea_3);
		add(new IntSpinner(1, 1, 10, 1, "Octave difference", n -> target.melodySpan[3]=n ));

		
/*		SpinnerNumberModel length = new SpinnerNumberModel(1, 1, 10, 1);
		JSpinner melodyLength = new JSpinner(length);
		add(melodyLength);
		
		SpinnerNumberModel count = new SpinnerNumberModel(1, 1, 10, 1);
		JSpinner tasksPerKey = new JSpinner(count);
		add(tasksPerKey); */
	}
	
	/* COMMANDS */
	
	/**
	 * A combination of a label and a spinner used to change
	 * the value of a targeted <code>int</code> variable.
	 * 
	 * @author Sorondil
	 *
	 */
	private class IntField extends JPanel implements ChangeListener {
		
		final JLabel caption;
		final JSpinner spinner;
		final IntConsumer action;
		
		/**
		 * Constructs a new NumField
		 * @param def The default value in the spinner to be displayed.
		 * @param min The minimum value available.
		 * @param max The maximum value available.
		 * @param step An increment of the value.
		 * @param captionText The text to be displayed in the JLabel.
		 * @param action Action to be executed.
		 */
		IntField(int def, int min, int max, int step, String captionText, IntConsumer action) {
			this.action = action;
			
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			setMaximumSize(new Dimension(200, 24));
			
			caption = new JLabel(captionText);
			add(caption);
			
			SpinnerNumberModel count = new SpinnerNumberModel(def, min, max, step);
			spinner = new JSpinner(count);
			spinner.addChangeListener(this);
			add(spinner);
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			int newValue = (int)((JSpinner)(e.getSource())).getValue();
			System.out.println("NumField changed to "+newValue);
			action.accept(newValue);
		}
	}
	
	/** An extension of the native JCheckBox with functionality added. */ 
	private class CheckBox extends JCheckBox implements ChangeListener {
		
		final IntConsumer action;
		
		/** Constructs a new CheckBox */
		CheckBox(String text, boolean selected, IntConsumer action) {
			super(text, selected);
			super.addChangeListener(this);
			this.action = action;
		}
		
		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			int newValue = (int)((JSpinner)(e.getSource())).getValue();
			System.out.println("NumField changed to "+newValue);
			action.accept(newValue);
		}
	}
	/** Sets the preferences object targeted by this GUI. */
	void setTarget(Preferences target) {
		this.target = target;
	}
	
}
