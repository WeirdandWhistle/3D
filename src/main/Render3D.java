package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import libs.MathUtil;
import libs.Struct;
import libs.Struct.Edge;
import libs.Struct.Face;
import libs.Struct.Obj;
import libs.Struct.Pixel;
import libs.Struct.Point2D;
import libs.Struct.Point3D;
import libs.Util;

public class Render3D {

	public Render3D() {

	}
	/**
	 * @return Double[][] / matrix nessacary for the wire frame rendering
	 **/
	public Double[][] renderScence(Panel p, BufferedImage frame, Face[] faces, Obj obj,
			Point3D[] points) {
		long timeBegin = System.currentTimeMillis();
		long timeEnd = System.currentTimeMillis();
		Graphics2D g2d = frame.createGraphics();

		g2d.setColor(Color.PINK);

		g2d.fillRect(0, 0, p.size.width, p.size.width);
		// System.out.println("cam pos: " + cam.pos.x + ", " + cam.pos.y
		// +
		// "," +
		// cam.pos.z);
		// MathUtil.Mat.print(obj.transformMat);
		p.cam.setViewXYZ(p.viewObject.translationVec, p.viewObject.rotationVec);
		Double[][] mat = obj.mat4();
		// MathUtil.Mat.print("paint: ", mat);
		// cam.orthographicProjection(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
		p.cam.perspectiveProjection(Math.toRadians(45), (double) p.size.width / p.size.height,
				p.nearPlane, p.farPlane);

		Double[][] projectionView = MathUtil.Mat.multi(p.cam.getView(), p.cam.getProjection());
		// try
		// reversing
		// the
		// order

		mat = MathUtil.Mat.multi(mat, projectionView);

		// System.out.println("cam z: " + viewObject.translationVec[2]);
		// System.out.println("camMat z: " + projectionView[2][2]);

		Pixel zBuffer[][] = new Pixel[p.size.width][p.size.height];

		Face[] polys = Util.Poly.getPolys(faces);
		faces = Util.Poly.getTri(faces);

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

			Point2D A = p.projectionA(a);
			Point2D B = p.projectionA(b);
			Point2D C = p.projectionA(c);

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
			maxx = Math.min(maxx, p.size.width - 1);
			maxy = Math.min(maxy, p.size.height - 1);

			maxx = Math.clamp(maxx, 0, p.size.width);
			maxy = Math.clamp(maxy, 0, p.size.height);

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
			// Double alpha = ((b.y - c.y) * (target.x - C.x) + (C.x - B.x) *
			// (target.y - C.y))
			// / ((A.y - C.y) * (B.x - C.x) + (C.x - A.x) * (C.y - B.y));
			// // System.out.println("loaded aplpha");
			// Double beta = ((C.y - A.y) * (target.x - C.x) + (A.x - C.x) *
			// (target.y - C.y))
			// / ((A.y - C.y) * (B.x - C.x) + (C.x - A.x) * (C.y - B.y));
			// // System.out.println("loaded beta");
			// Double gamma = 1 - alpha - beta;
			// // System.out.println("loaded gamma");
			//
			// Double zed = (alpha * (a.z * 100 + 250))// comment
			// + (beta * (b.z * 100 + 250))// comment sthing
			// + (gamma * (c.z * 100 + 250));
			//
			// zed = Math.floor(zed + 1);

			Double zed = (a.z + b.z + c.z) / 3;
			// System.out.println("tri: " + zed);

			int renderTri[][] = MathUtil.tri.getTrianglePixels(new Point2D(A.x - minx, A.y - miny),
					new Point2D(B.x - minx, B.y - miny), new Point2D(C.x - minx, C.y - miny),
					maxx - minx, maxy - miny);

			for (int x = minx; x < maxx; x++) {
				for (int y = miny; y < maxy; y++) {

					if (maxx - minx > renderTri.length || maxy - miny > renderTri[0].length) {
						p.reportError("Null pointer exseption. overflow at renderTri maxy - miny: "
								+ (maxy - miny) + " Length[0]: " + renderTri[0].length + ", maxy: "
								+ maxy + ", miny: " + miny, 50);
						p.reportError("Null pointer exseption. overflow at renderTri maxx - minx: "
								+ (maxx - minx) + " Length: " + renderTri.length + " maxx: " + maxx
								+ " minx: " + minx, 40);
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
							p.reportError("none", 5);
						}
					} else {
						// System.out.println("naddadaa");
						p.reportError("no case before triggered", 2);
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
		if (points != null) {

			if (polys.length > 0) {

				Pixel[][] runningZed = (Pixel[][]) Util.popArray2Dzb(
						new Pixel(1000.0, new Color(100, 100, 100, 100)), p.size.width,
						p.size.height);
				for (Face face : polys) {

					runningZed = Util.comparePixelArray(runningZed,
							renerPolygon(p, frame, face, points, mat));
					// Util.printZBuffer("runningZed", runningZed);
				}
				zBuffer = Util.comparePixelArray(zBuffer, runningZed);
			}
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
					p.reportError("raserization zBuffer null", 1000);
				}
			}
		}

		// timeEnd = System.currentTimeMillis();
		// System.out.println("time took to render: " + (timeEnd - timeBegin));
		// System.out.println("currentThread: " + Thread.currentThread());
		// System.out.println("size of zBufer " + (zBuffer.length *
		// zBuffer[0].length));
		return mat;
	}

	public Pixel[][] renerPolygon(Panel p, BufferedImage frame, Face face, Point3D[] points,
			Double[][] mat) {
		Double[][] screenPoints = new Double[face.indices.length][2];
		Double avgZ = 0.0;

		for (int i = 0; i < face.indices.length; i++) {
			Double[][] affterCamPos = MathUtil.Mat.multi(points[face.getIndex(i)].getMat(), mat);

			screenPoints[i] = p.projectionA(affterCamPos[0]);
			// System.out.println("i " + screenPoints[i][0]);
			avgZ += affterCamPos[0][2];
		}
		avgZ /= face.indices.length;
		// System.out.println(screenPoints[4][0]);
		Polygon poly = Util.Poly.fromDouble(screenPoints);
		// frame.createGraphics().setColor(Color.pink);
		// frame.createGraphics().fillRect(poly.getBounds().x,
		// poly.getBounds().y,
		// poly.getBounds().width, poly.getBounds().height);
		// System.out.println("poly: " + avgZ);
		int[][] filledPixels = Util.Poly.fillPolyArray(poly, p.size.width, p.size.height);

		// MathUtil.print2DArray("filledPixels", filledPixels);

		Pixel[][] zBuffer = Util.popArray2Dzb(new Pixel(10000.0, new Color(100, 100, 100, 200)),
				p.size.width, p.size.height);

		for (int x = 0; x < filledPixels.length; x++) {
			for (int y = 0; y < filledPixels[0].length; y++) {
				if (filledPixels[x][y] == 1 && (zBuffer[x][y].zedBuffer > avgZ)) {
					zBuffer[x][y] = new Pixel(avgZ, face.getColor());
				}
			}
		}
		return zBuffer;

	}
	public void renderWireFrame(Panel p, Edge[] edges, Obj obj, Graphics2D g2d, Double[][] mat) {
		for (int i = 0; i < edges.length; i++) {
			Point3D startPoint = obj.points[edges[i].start];
			Point3D endPoint = obj.points[edges[i].end];
			// MathUtil.Mat.print("for loop: ", mat);
			startPoint = Struct.toPoint3D(MathUtil.Mat.multi(startPoint.getMat(), mat));
			endPoint = Struct.toPoint3D(MathUtil.Mat.multi(endPoint.getMat(), mat));

			Point2D start = p.projectionA(startPoint);
			Point2D end = p.projectionA(endPoint);

			g2d.setColor(Color.MAGENTA);

			g2d.drawLine((int) start.x.doubleValue(), (int) start.y.doubleValue(), // Comment
					(int) end.x.doubleValue(), (int) end.y.doubleValue());

		}
	}

}
