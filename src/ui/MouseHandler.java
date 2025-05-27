package ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.Panel;

public class MouseHandler implements MouseListener, MouseMotionListener {

	public Point m = new Point(0, 0);
	private Panel p;

	public MouseHandler(Panel p) {
		this.p = p;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch (e.getButton()) {
			case 1 :
				System.out.println("left click");
				break;
			case 2 :
				System.out.println("middle click");
				break;
			case 3 :
				System.out.println("right click");
				break;
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		m = e.getPoint();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		m = e.getPoint();

	}

}
