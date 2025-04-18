package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import libs.MathUtil;
import libs.Struct.Face;
import libs.Struct.Obj;
import libs.Struct.Point2D;
import libs.Struct.Point3D;
import load.OBJLoad;
import ui.KeyHandler;

public class Panel extends JPanel implements Runnable {

	public int gameTicks = 60;
	public Double FOV = 100.0;
	public Double nearPlane = 0.01;
	public Double farPlane = 10.0;
	public Double[] ones = {1.0, 1.0, 1.0};
	public double rot = 0;
	public double slow = 0.01;
	public double lastTime = System.currentTimeMillis();
	public int FPS = 0;
	private int displayFPS = FPS;
	public String error = null;
	public int errorVal = 0;
	private int t = 0;

	public Dimension size = new Dimension(500, 500);
	public Thread gameThread;

	public Camera cam = new Camera();
	public Obj viewObject = new Obj();
	public KeyHandler keys = new KeyHandler(this);
	public BufferedImage frame = new BufferedImage(size.width, size.height,
			BufferedImage.TYPE_INT_ARGB);
	private Render3D render = new Render3D();
	public Graphics2D g2d = frame.createGraphics();

	// public Point3D[] points = {// Comment
	// new Point3D(-1.0, -1.0, -1.0), new Point3D(-1.0, -1.0, 1.0), // Comment
	// new Point3D(1.0, -1.0, -1.0), new Point3D(-1.0, 1.0, -1.0), // Comment
	// new Point3D(-1.0, 1.0, 1.0), new Point3D(1.0, -1.0, 1.0), // Comment
	// new Point3D(1.0, 1.0, -1.0), new Point3D(1.0, 1.0, 1.0)};// Comment
	// public Edge edges[] = { // Comment
	// new Edge(0, 1), new Edge(0, 2), new Edge(0, 3), // Comment
	// new Edge(2, 5), new Edge(3, 6), new Edge(3, 4), // Comment
	// new Edge(4, 7), new Edge(6, 7), new Edge(7, 5), // Comment
	// new Edge(5, 1), new Edge(4, 1), new Edge(2, 6)};
	// public Face faces[] = {// commmmmmmmmmmeeeeeeeeennnnnnnnnntttttttt
	// new Face(new int[]{0, 1, 5, 2}, Color.red),
	// new Face(new int[]{3, 4, 7, 6}, Color.orange),
	// new Face(new int[]{4, 1, 5, 7}, Color.blue),
	// new Face(new int[]{0, 2, 6, 3}, Color.green),
	// new Face(new int[]{2, 6, 7, 5}, Color.yellow),
	// new Face(new int[]{3, 0, 1, 4}, Color.white)};

	public ArrayList<Point3D[]> points = new ArrayList<>();
	public ArrayList<Face[]> faces = new ArrayList<>();

	public Obj obj;
	public OBJLoad fileLoad = new OBJLoad(new File("assets\\primitives\\cube.obj"));

	public Panel() {
		// cam.setViewDirection(new Double[]{0.0, 0.0, 0.0}, new Double[]{0.0,
		// 0.0, 1.0},
		// new Double[]{0.0, -1.0, 0.0});
		// cam.setViewTarget(new Double[]{-1.0, -2.0, 2.0}, new Double[]{0.0,
		// 1.0, 0.0}, null);
		this.setFocusTraversalKeysEnabled(false);
		this.setPreferredSize(size);
		this.loadGame();
		this.startGameThread();

	}
	public void renderFrame() {
		Double[][] mat = render.renderScence(this, frame, faces, obj, points);
		// render.renderWireFrame(this, edges, obj, g2d, mat);

	}
	public void drawToScreen() {
		// System.out.println(this.getGraphics());
		Graphics g = this.getGraphics();
		// System.out.println(g)
		// g.drawRect(0, 0, 300, 300);;
		if (g != null) {

			g.drawImage(frame, 0, 0, size.width, size.height, null);
			g.setColor(Color.black);
			g.drawString(Integer.toString(displayFPS), 0, g.getFontMetrics().getHeight());
			FPS++;
		}

	}
	public void loadGame() {
		System.out.println("started loading game " + Thread.currentThread());
		fileLoad.load();

		points.add(fileLoad.getPoints());
		faces.add(fileLoad.getFaces());

		obj = new Obj(points.get(0), new Double[]{0.0, 0.0, 3.0}, new Double[]{0.0, 0.0, 0.0},
				new Double[]{0.6, 0.6, 0.6});
		System.out.println("fineshed loading game");
		// System.out.println(faces.get(0).length);

	}

	public void update() {

		keys.update();

		rot += slow;
		// obj.rotate(rot, rot / 2, 0.0);
		// obj.translationVec[2] += sn low;

	}

	@Override
	public void run() {
		double drawInterval = 1000000000 / gameTicks;
		double nextdrawTime = System.nanoTime() + drawInterval;

		while (gameThread != null) {

			if (System.currentTimeMillis() > this.lastTime + 1000) {
				// System.out.println("time: " + System.currentTimeMillis());
				displayFPS = FPS;
				String imError = this.throwError();

				if (imError != null && !imError.equals("none")) {
					System.out.println(imError);
				}

				this.lastTime = System.currentTimeMillis();
				FPS = 0;
			}

			update();
			renderFrame();
			drawToScreen();

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
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	public Point2D projectionA(Point3D point) {
		return new Point2D(MathUtil.pCord(point.x, FOV, point.z) * 100 + 250,
				MathUtil.pCord(point.y, FOV, point.z) * 100 + 250);
	}
	public Double[] projectionA(Double[] point3D) {
		return new Double[]{MathUtil.pCord(point3D[0], FOV, point3D[2]) * 100 + 250,
				MathUtil.pCord(point3D[1], FOV, point3D[2]) * 100 + 250};
	}
	public Point2D projectionB(Point3D point) {
		return new Point2D(point.x * 100 + size.width / 2, point.y * 100 + size.height / 2);
	}
	public void reportError(String error, int value) {

		if (this.error != error) {
			if (this.errorVal < value) {
				this.error = error;
				errorVal = value;
			}
			// System.out.println("ERROR: " + error);
		}
	}
	public String throwError() {
		if (error != null) {
			// System.out.println("ERROR: " + error);
			error = null;
			errorVal = 0;
			return error;
		}
		return null;
	}

}
