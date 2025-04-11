package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import mathUtil.MathUtil;
import mathUtil.Struct;
import mathUtil.Struct.Edge;
import mathUtil.Struct.Face;
import mathUtil.Struct.Pixel;
import mathUtil.Struct.Point2D;
import mathUtil.Struct.Point3D;
import mathUtil.Struct.obj;
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
	public String error = null;
	public int errorVal = 0;
	private int t = 0;

	public Dimension size = new Dimension(500, 500);
	public Thread gameThread;

	public Camera cam = new Camera();
	public obj viewObject = new obj();
	public KeyHandler keys = new KeyHandler(this);
	public BufferedImage frame = new BufferedImage(size.width, size.height,
			BufferedImage.TYPE_INT_ARGB);
	public Graphics2D g2d = frame.createGraphics();

	public Point3D[] points = {// Comment
			new Point3D(-1.0, -1.0, -1.0), new Point3D(-1.0, -1.0, 1.0), // Comment
			new Point3D(1.0, -1.0, -1.0), new Point3D(-1.0, 1.0, -1.0), // Comment
			new Point3D(-1.0, 1.0, 1.0), new Point3D(1.0, -1.0, 1.0), // Comment
			new Point3D(1.0, 1.0, -1.0), new Point3D(1.0, 1.0, 1.0)};// Comment
	public Edge edges[] = { // Comment
			new Edge(0, 1), new Edge(0, 2), new Edge(0, 3), // Comment
			new Edge(2, 5), new Edge(3, 6), new Edge(3, 4), // Comment
			new Edge(4, 7), new Edge(6, 7), new Edge(7, 5), // Comment
			new Edge(5, 1), new Edge(4, 1), new Edge(2, 6)};
	public Face faces[] = {// commmmmmmmmmmeeeeeeeeennnnnnnnnntttttttt
			new Face(new int[]{0, 1, 2}, Color.red), new Face(new int[]{1, 2, 5}, Color.red),
			new Face(new int[]{3, 4, 6}, Color.orange), new Face(new int[]{7, 4, 6}, Color.orange),
			new Face(new int[]{4, 5, 7}, Color.blue), new Face(new int[]{4, 5, 1}, Color.blue),
			new Face(new int[]{0, 2, 3}, Color.green), new Face(new int[]{2, 3, 6}, Color.green),
			new Face(new int[]{2, 6, 7}, Color.yellow), new Face(new int[]{2, 5, 7}, Color.yellow),
			new Face(new int[]{3, 0, 1}, Color.white), new Face(new int[]{3, 4, 1}, Color.white)};

	public obj obj = new obj(points, new Double[]{0.0, 0.0, 1.0}, new Double[]{0.0, 0.0, 0.0},
			new Double[]{0.5, 0.5, 0.5});

	public Panel() {
		cam.setViewDirection(new Double[]{0.0, 0.0, 0.0}, new Double[]{0.0, 0.0, 1.0},
				new Double[]{0.0, -1.0, 0.0});
		// cam.setViewTarget(new Double[]{-1.0, -2.0, 2.0}, new Double[]{0.0,
		// 1.0, 0.0}, null);

		this.setPreferredSize(size);
		this.startGameThread();
	}
	public void renderFrame() {
		long timeBegin = System.currentTimeMillis();
		long timeEnd = System.currentTimeMillis();
		g2d.setColor(Color.PINK);

		g2d.fillRect(0, 0, size.width, size.width);
		// System.out.println("cam pos: " + cam.pos.x + ", " + cam.pos.y
		// +
		// "," +
		// cam.pos.z);
		// MathUtil.Mat.print(obj.transformMat);
		cam.setViewXYZ(viewObject.translationVec, viewObject.rotationVec);
		Double[][] mat = obj.mat4();
		// MathUtil.Mat.print("paint: ", mat);
		// cam.orthographicProjection(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
		cam.perspectiveProjection(Math.toRadians(50), (double) size.width / size.height, nearPlane,
				farPlane);

		Double[][] projectionView = MathUtil.Mat.multi(cam.getView(), cam.getProjection());
		// try
		// reversing
		// the
		// order
		MathUtil.print2DArray(mat);
		System.out.println();
		mat = MathUtil.Mat.multi(mat, projectionView);
		MathUtil.print2DArray(mat);
		System.out.println();

		Pixel zBuffer[][] = new Pixel[size.width][size.height];

		// Initialize each element with a default Pixel (e.g., with an
		// initial
		// z-value)
		for (int x = 0; x < zBuffer.length; x++) {
			for (int y = 0; y < zBuffer[0].length; y++) {
				zBuffer[x][y] = new Pixel(10000.0, new Color(100, 100, 100, 100)); // Initiali
			}
		}

		for (int j = 0; j < faces.length; j++) {

			Point3D a = obj.points[faces[j].getIndex(0)];
			Point3D b = obj.points[faces[j].getIndex(1)];
			Point3D c = obj.points[faces[j].getIndex(2)];

			a = Struct.toPoint3D(MathUtil.Mat.multi(a.getMat(), mat));
			b = Struct.toPoint3D(MathUtil.Mat.multi(b.getMat(), mat));
			c = Struct.toPoint3D(MathUtil.Mat.multi(c.getMat(), mat));

			Point2D A = this.projectionB(a);
			Point2D B = this.projectionB(b);
			Point2D C = this.projectionB(c);

			Point2D target;

			if (a.z > b.z) {
				if (a.z > c.z) {
					target = A;
				} else {
					target = C;
				}
			} else {
				if (b.z > c.z) {
					target = B;
				} else {
					target = C;
				}
			}

			int minx = (int) Math.floor(Math.min(A.x, Math.min(B.x, C.x)));
			int miny = (int) Math.floor(Math.min(A.y, Math.min(B.y, C.y)));
			int maxx = (int) Math.ceil(Math.max(A.x, Math.max(B.x, C.x)));
			int maxy = (int) Math.ceil(Math.max(A.y, Math.max(B.y, C.y)));

			// Ensure the bounding box stays within screen bounds
			minx = Math.max(minx, 0);
			miny = Math.max(miny, 0);
			maxx = Math.min(maxx, size.width - 1);
			maxy = Math.min(maxy, size.height - 1);

			maxx = Math.clamp(maxx, 0, size.width);
			maxy = Math.clamp(maxy, 0, size.height);

			// g2d.setColor(Color.pink);
			// g2d.drawRect(minx, miny, maxx - minx, maxy - miny);
			// System.out.println("minx: " + minx + ", miny: " + miny +
			// ",
			// maxx:
			// " + maxx + ", maxy: "
			// + maxy + " , color: " + faces[j].getColor());
			// g2d.setColor(Color.yellow);
			// g2d.fillPolygon(
			// new int[]{(int) A.x.doubleValue(), (int)
			// B.x.doubleValue(),
			// (int) C.x.doubleValue()},
			// new int[]{(int) A.y.doubleValue(), (int)
			// B.y.doubleValue(),
			// (int) C.y.doubleValue()},
			// 3);

			// System.out.println("zbuffer != null");
			Double alpha = ((b.y - c.y) * (target.x - C.x) + (C.x - B.x) * (target.y - C.y))
					/ ((A.y - C.y) * (B.x - C.x) + (C.x - A.x) * (C.y - B.y));
			// System.out.println("loaded aplpha");
			Double beta = ((C.y - A.y) * (target.x - C.x) + (A.x - C.x) * (target.y - C.y))
					/ ((A.y - C.y) * (B.x - C.x) + (C.x - A.x) * (C.y - B.y));
			// System.out.println("loaded beta");
			Double gamma = 1 - alpha - beta;
			// System.out.println("loaded gamma");

			Double zed = (alpha * (a.z * 100 + 250))// comment
					+ (beta * (b.z * 100 + 250))// comment sthing
					+ (gamma * (c.z * 100 + 250));

			zed = Math.floor(zed + 1);

			zed = a.z + b.z + c.z / 3;

			int renderTri[][] = MathUtil.tri.getTrianglePixels(new Point2D(A.x - minx, A.y - miny),
					new Point2D(B.x - minx, B.y - miny), new Point2D(C.x - minx, C.y - miny),
					maxx - minx, maxy - miny);

			for (int x = minx; x < maxx; x++) {
				for (int y = miny; y < maxy; y++) {

					if (maxx - minx > renderTri.length || maxy - miny > renderTri[0].length) {
						this.reportError(
								"Null pointer exseption. overflow at renderTri maxy - miny: "
										+ (maxy - miny) + " Length[0]: " + renderTri[0].length
										+ ", maxy: " + maxy + ", miny: " + miny,
								50);
						this.reportError(
								"Null pointer exseption. overflow at renderTri maxx - minx: "
										+ (maxx - minx) + " Length: " + renderTri.length + " maxx: "
										+ maxx + " minx: " + minx,
								40);
					} else if (zBuffer[x][y] == null) {

						if (renderTri[x - minx][y - miny] == 1) {
							// sep
							zBuffer[x][y] = new Pixel(zed, faces[j].getColor());
						}
					}
					// System.out.print(" ," + zBuffer[x][y].zedBuffer);
					else if (renderTri[x - minx][y - miny] == 1) {
						// System.out.println("zBuffer[x][y].zedBuffer:
						// " +
						// zBuffer[x][y].zedBuffer);
						if (zBuffer[x][y].zedBuffer > zed) {
							zBuffer[x][y] = new Pixel(zed, faces[j].getColor());
							// System.out.println("yessssir!!!!");
							this.reportError("none", 5);
						}
					} else {
						// System.out.println("naddadaa");
						this.reportError("no case before triggered", 2);
					}
				}
				// MathUtil.print2DArray("redmetri: ", renderTri);
			}
			// System.out.println("finshed loading the zBUffer");

			//
			// g2d.setColor(faces[j].getColor());
			// g2d.fillPolygon(Struct.toPointsX(one, two, three),
			// Struct.toPointsY(one, two, three),
			// 3);
			// MathUtil.print2DArray("renderTri", renderTri);

		}

		// timeBegin = System.currentTimeMillis();
		// Color defaultColor = new Color(100, 100, 100, 10);
		for (int x = 0; x < zBuffer.length; x++) {
			for (int y = 0; y < zBuffer[0].length; y++) {
				if (zBuffer[x][y] != null) {
					// g2d.setColor();
					// g2d.setColor(Color.yellow);
					frame.setRGB(x, y,
							(zBuffer[x][y].zedBuffer > 10000 || zBuffer[x][y].zedBuffer < 0
									? new Color(100, 100, 100, 0)
									: zBuffer[x][y].color).getRGB());

				} else {
					this.reportError("raserization zBuffer null", 1000);
				}
			}
		}
		for (int i = 0; i < edges.length; i++) {
			Point3D startPoint = obj.points[edges[i].start];
			Point3D endPoint = obj.points[edges[i].end];
			// MathUtil.Mat.print("for loop: ", mat);
			startPoint = Struct.toPoint3D(MathUtil.Mat.multi(startPoint.getMat(), mat));
			endPoint = Struct.toPoint3D(MathUtil.Mat.multi(endPoint.getMat(), mat));

			Point2D start = this.projectionB(startPoint);
			Point2D end = this.projectionB(endPoint);

			g2d.setColor(Color.MAGENTA);

			g2d.drawLine((int) start.x.doubleValue(), (int) start.y.doubleValue(), // Comment
					(int) end.x.doubleValue(), (int) end.y.doubleValue());

		}
		// timeEnd = System.currentTimeMillis();
		// System.out.println("time took to render: " + (timeEnd - timeBegin));
		// System.out.println("currentThread: " + Thread.currentThread());
		// System.out.println("size of zBufer " + (zBuffer.length *
		// zBuffer[0].length));
	}
	public void drawToScreen() {
		// System.out.println(this.getGraphics());
		Graphics g = this.getGraphics();
		// System.out.println(g)
		// g.drawRect(0, 0, 300, 300);;
		if (g != null) {
			g.drawImage(frame, 0, 0, size.width, size.height, null);
		}
		FPS++;
	}

	public void update() {

		keys.update();

		rot += slow;
		// obj.rotate(rot, rot / 2, 0.0);

	}

	@Override
	public void run() {
		double drawInterval = 1000000000 / gameTicks;
		double nextdrawTime = System.nanoTime() + drawInterval;

		while (gameThread != null) {

			if (System.currentTimeMillis() > this.lastTime + 1000) {
				// System.out.println("time: " + System.currentTimeMillis());
				System.out.println("FPS: " + FPS);
				this.throwError();
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
	public void throwError() {
		if (error != null) {
			System.out.println("ERROR: " + error);
			error = null;
			errorVal = 0;
		}
	}

}
