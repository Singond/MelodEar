package cz.slanyj.earTrainer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

class ButtonBoard extends JPanel implements ActionListener {

	private class Key extends JButton {
		
		Key(String label, Note note) {
			super(label);
			this.note = note;
		}
		
		private Note note;
		
		void play() {
				Sound.play(note);
		}
	}
	
	Key buttonC;
	Key buttonD;
	Key buttonE;
	Key buttonF;
	Key buttonG;
	Key buttonA;
	Key buttonH;
	Key buttonC2;
	
	ButtonBoard() {
		buttonC = new Key("Note C", new Note(PianoKey.C,4));
		buttonC.addActionListener(this);
		add(buttonC);
		buttonD = new Key("Note D", new Note(PianoKey.D,4));
		buttonD.addActionListener(this);
		add(buttonD);
		buttonE = new Key("Note E", new Note(PianoKey.E,4));
		buttonE.addActionListener(this);
		add(buttonE);
		buttonF = new Key("Note F", new Note(PianoKey.F,4));
		buttonF.addActionListener(this);
		buttonF.setBackground(Color.BLUE);
		add(buttonF);
		buttonG = new Key("Note G", new Note(PianoKey.G,4));
		buttonG.addActionListener(this);
		add(buttonG);
		buttonA = new Key("Note A", new Note(PianoKey.A,4));
		buttonA.addActionListener(this);
		add(buttonA);
		buttonH = new Key("Note H", new Note(PianoKey.H,4));
		buttonH.addActionListener(this);
		add(buttonH);
		buttonC2 = new Key("Note C", new Note(PianoKey.C,5));
		buttonC2.addActionListener(this);
		add(buttonC2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Key clicked = (Key)(e.getSource());
		clicked.play();
	}
}
