package ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import libs.Struct.Obj;
import main.Panel;

public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {

	public Point m = new Point(0, 0);
	public Point pm = new Point(0, 0);
	private Panel p;
	private MouseEvent e = null;

	public MouseHandler(Panel p) {
		this.p = p;
	}

	public void update() {

		double xScale = (double) p.size.width / p.drawSize.width;
		double yScale = (double) p.size.height / p.drawSize.height;
		double mpx = m.x * xScale;
		double mpy = m.y * yScale;

		// System.out.println("xSclae:" + xScale);
		// System.out.println(p.size.width / p.drawSize.width);

		pm.setLocation((int) mpx, (int) mpy);

		// System.out.println("x:" + pm.x + ", y:" + pm.y);

		if (e != null) {

			switch (e.getButton()) {
				case 1 :
					System.out.println("left click " + Thread.currentThread());
					for (Obj obj : p.objs) {
						if (obj.hoverOver) {
							obj.focused = true;
						} else {
							obj.focused = false;
						}
					}
					break;
				case 2 :
					System.out.println("middle click");
					break;
				case 3 :
					System.out.println("right click");
					break;
			}
		}
		e = null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		this.e = e;

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
		// pm.x = m.x * (p.size.width / p.drawSize.width);
		// pm.y = m.y * (p.size.height / p.drawSize.height);

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		m = e.getPoint();
		// pm.x = m.x * (p.size.width / p.drawSize.width);
		// pm.y = m.y * (p.size.height / p.drawSize.height);

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		System.out.println("mouse:" + e.getWheelRotation());

		p.zoomScale += e.getWheelRotation();
		System.out.println("zoomScale:" + p.zoomScale);

	}

}
