package main;

import java.awt.KeyboardFocusManager;

import javax.swing.JFrame;

public class Frame extends JFrame {

	Panel Panel = new Panel();

	public Frame() {
		this.setVisible(true);
		this.requestFocus();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("doom rip off on cpu");
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		this.addKeyListener(Panel.keys);
		this.add(Panel);

		this.pack();
	}

}
