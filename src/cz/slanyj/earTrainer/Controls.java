package cz.slanyj.earTrainer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * A panel with controls for the trainer.
 */
public class Controls extends JPanel {

	private Trainer trainer;
	
	/** Constructs the control panel with default settings. */
	Controls() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		setPreferredSize(new Dimension(200, 300));
		
		JButton startBut = new JButton(start);
		startBut.setToolTipText("Start ear training");
		startBut.setMaximumSize(new Dimension(120, 40));
		startBut.setText("Start");
		add(startBut);
		
		JButton finishBut = new JButton(finish);
		finishBut.setMaximumSize(new Dimension(120, 40));
		finishBut.setText("Finish");
		add(finishBut);
		
		JButton changeKeyBut = new JButton(changeKey);
		changeKeyBut.setMaximumSize(new Dimension(120, 40));
		changeKeyBut.setText("New Key");
		add(changeKeyBut);
		
		JButton newTaskBut = new JButton(newTask);
		newTaskBut.setMaximumSize(new Dimension(120, 40));
		newTaskBut.setText("New Task");
		add(newTaskBut);
		
		JButton replayKeyBut = new JButton(replayKey);
/*		replayKeyBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});*/
		replayKeyBut.setMaximumSize(new Dimension(120, 40));
		replayKeyBut.setText("Replay Key");
		add(replayKeyBut);
		
		JButton replayTaskBut = new JButton(replayTask);
		replayTaskBut.setMaximumSize(new Dimension(120, 40));
		replayTaskBut.setText("Replay Task");
		add(replayTaskBut);
	}
	
	/* COMMANDS */

	AbstractAction start = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				trainer.start();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (NullPointerException e2) {
				System.out.println("The trainer has not been set");
			}
		}
	};
	
	AbstractAction finish = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			trainer.stopExc();
		}
	};
	
	AbstractAction changeKey = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			trainer.newKey(0);
		}
	};
	
	AbstractAction newTask = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			trainer.newTask(0);
		}
	};
	
	AbstractAction replayKey = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			trainer.playKey(0);
		}
	};
	
	AbstractAction replayTask = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			trainer.playTask(0);
		}
	};
	
	
	void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}
	
	
	private class Button extends JButton {
		
	}
	
}
