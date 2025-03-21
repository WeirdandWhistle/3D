package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Panel;

public class KeyHandler implements KeyListener {

	public boolean w, a, s, d, shift, crtl, space;
	public Double startSpeed = 0.07;
	public Double speed = startSpeed;
	public Double sprintSpeed = speed * 1.5;
	private Panel p;

	public KeyHandler(Panel p) {
		this.p = p;
	}
	public void update() {
		if (shift) {
			speed = sprintSpeed;
		} else {
			speed = startSpeed;
		}
		if (w) {
			p.cam.moveZ(speed);
		}
		if (a) {
			p.cam.moveX(-speed);
		}
		if (s) {
			p.cam.moveZ(-speed);
		}
		if (d) {
			p.cam.moveX(speed);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// System.out.println(e.getKeyChar() + " : " + e.getKeyCode());
		boolean toggle = true;
		switch (e.getKeyCode()) {
			case 87 :
				w = toggle;
				break;
			case 65 :
				a = toggle;
				break;
			case 83 :
				s = toggle;
				break;
			case 68 :
				d = toggle;
				break;
			case 16 :
				shift = toggle;
				break;
			case 32 :
				space = toggle;
				break;
			case 17 :
				crtl = toggle;
				break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		boolean toggle = false;
		switch (e.getKeyCode()) {
			case 87 :
				w = toggle;
				break;
			case 65 :
				a = toggle;
				break;
			case 83 :
				s = toggle;
				break;
			case 68 :
				d = toggle;
				break;
			case 16 :
				shift = toggle;
				break;
			case 32 :
				space = toggle;
				break;
			case 17 :
				crtl = toggle;
				break;
		}

	}

}
