package cz.slanyj.earTrainer;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

class Display extends JPanel {
	private static final long serialVersionUID = 1L;
	
	JLabel result;
	Font font = new Font("Serif", Font.BOLD, 24);
	Color correct = Color.BLUE;
	Color wrong = Color.RED;
//		Color good = Color.GREEN;
	Color neutral = Color.BLACK;
	
	Display() {
		result = new JLabel();
		result.setFont(font);
//			setPreferredSize(new Dimension(60, 300));
		add(result);
	}
	
	synchronized void clear() {
		result.setText("");
	}
	synchronized void correct() {
		result.setText("");
		result.setForeground(correct);
		result.setText("CORRECT");
	}
	synchronized void wrong() {
		result.setText("");
		result.setForeground(wrong);
		result.setText("WRONG");
	}
	synchronized void goodSoFar() {
		result.setText("");
		result.setForeground(neutral);
		result.setText("So far, so good…");
	}
}