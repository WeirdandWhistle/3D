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

		Face[] polys = faces;

		// Initialize each element with a default Pixel (e.g., with an
		// initial
		// z-value)
		for (int x = 0; x < zBuffer.length; x++) {
			for (int y = 0; y < zBuffer[0].length; y++) {
				zBuffer[x][y] = new Pixel(10000.0, new Color(100, 100, 100, 100)); // Initiali
			}
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
