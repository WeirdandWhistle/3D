package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import mathUtil.MathUtil;
import mathUtil.Struct;
import mathUtil.Struct.Edge;
import mathUtil.Struct.Point2D;
import mathUtil.Struct.Point3D;

public class Panel extends JPanel implements Runnable {

	public int gameTicks = 60;
	public Double FOV = 10.0;
	public double rot = 0;
	public double slow = 0.01;

	public Dimension size = new Dimension(500, 500);
	public Thread gameThread;
	public Struct s = new Struct();

	public Point3D[] points = {// Comment
			s.new Point3D(-1.0, -1.0, -1.0), s.new Point3D(-1.0, -1.0, 1.0),
			s.new Point3D(1.0, -1.0, -1.0), s.new Point3D(-1.0, 1.0, -1.0),
			s.new Point3D(-1.0, 1.0, 1.0), s.new Point3D(1.0, -1.0, 1.0),
			s.new Point3D(1.0, 1.0, -1.0), s.new Point3D(1.0, 1.0, 1.0)};
	public Edge edges[] = { // Comment
			s.new Edge(0, 1), s.new Edge(0, 2), s.new Edge(0, 3), // Comment
			s.new Edge(2, 5), s.new Edge(3, 6), s.new Edge(3, 4), // Comment
			s.new Edge(4, 7), s.new Edge(6, 7), s.new Edge(7, 5), // Comment
			s.new Edge(5, 1), s.new Edge(4, 1), s.new Edge(2, 6)};

	public Panel() {
		this.setPreferredSize(size);
		this.startGameThread();

	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, size.width, size.width);

		g2d.setColor(Color.white);
		System.out.println(rot);
		for (int i = 0; i < edges.length; i++) {
			Point3D startPoint = s.toPoint3D(MathUtil.Mat
					.rotX(MathUtil.Mat.rotY(points[edges[i].start].getMat(), rot), rot));
			Point3D endPoint = s.toPoint3D(
					MathUtil.Mat.rotX(MathUtil.Mat.rotY(points[edges[i].end].getMat(), rot), rot));

			Point2D start = this.projection(startPoint);
			Point2D end = this.projection(endPoint);

			g2d.drawLine((int) start.x.doubleValue(), (int) start.y.doubleValue(), // Comment
					(int) end.x.doubleValue(), (int) end.y.doubleValue());
		}
	}
	public Point2D projection(Point3D point) {
		return s.new Point2D(MathUtil.pCord(point.x, FOV, point.z) * 100 + 250,
				MathUtil.pCord(point.y, FOV, point.z) * 100 + 250);
	}

	public void update() {
		this.repaint();
		rot += slow;
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		double drawInterval = 1000000000 / gameTicks;
		double nextdrawTime = System.nanoTime() + drawInterval;

		while (gameThread != null) {

			update();

			try {

				double remaningTime = nextdrawTime - System.nanoTime();
				remaningTime = remaningTime / 1000000;

				if (remaningTime < 0) {
					remaningTime = 0;
				}

				Thread.sleep((long) remaningTime);

				nextdrawTime += drawInterval;
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}

	}

}
