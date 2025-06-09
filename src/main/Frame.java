package main;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Frame extends JFrame {

	Panel Panel;

	public Frame() {
		Panel = new Panel();
		this.setVisible(true);
		this.requestFocus();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("doom rip off on cpu");
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setFocusTraversalKeysEnabled(false);
		this.addKeyListener(Panel.keys);
		this.setAutoRequestFocus(true);

		this.setBackground(Color.orange);

		this.add(Panel);

		this.pack();
	}

}
