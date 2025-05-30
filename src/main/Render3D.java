package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import libs.MathUtil;
import libs.Struct.Face;
import libs.Struct.Obj;
import libs.Struct.Pixel;
import libs.Struct.Point3D;
import libs.Util;

public class Render3D {

	public Render3D() {

	}
	/**
	 * @return Double[][] / matrix nessacary for the wire frame rendering
	 **/
	public void renderScence(Panel p, BufferedImage frame, ArrayList<Obj> objs) {
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

		// MathUtil.Mat.print("paint: ", mat);
		// cam.orthographicProjection(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
		p.cam.perspectiveProjection(Math.toRadians(45), (double) p.size.width / p.size.height,
				p.nearPlane, p.farPlane);

		Double[][] projectionView = MathUtil.Mat.multi(p.cam.getView(), p.cam.getProjection());

		Pixel zBuffer[][] = new Pixel[p.size.width][p.size.height];
		for (int x = 0; x < zBuffer.length; x++) {
			for (int y = 0; y < zBuffer[0].length; y++) {
				zBuffer[x][y] = new Pixel(10000.0, new Color(100, 100, 100, 100)); // Initiali
			}
		}

		for (int o = 0; o < objs.size(); o++) {

			Double[][] mat = objs.get(o).mat4();
			mat = MathUtil.Mat.multi(mat, projectionView);

			objs.get(o).toCamSpaceMat = mat;

			// System.out.println("cam z: " + viewObject.translationVec[2]);
			// System.out.println("camMat z: " + projectionView[2][2]);

			// Initialize each element with a default Pixel (e.g., with an
			// initial
			// z-value)

			if (objs.get(o).points != null) {
				Pixel[][] runningZed = (Pixel[][]) Util.popArray2Dzb(
						new Pixel(1000.0, new Color(100, 100, 100, 100)), p.size.width,
						p.size.height);

				Face[] polys = objs.get(o).faces;
				if (polys.length > 0) {

					// System.out.println(polys[6]);
					// timeBegin = System.currentTimeMillis();
					int f = 0;
					for (Face face : polys) {
						// System.out.println(k);

						renerPolygon(p, frame, face, objs.get(o).points, mat, runningZed,
								objs.get(o), f);

						// runningZed = Util.comparePixelArray(runningZed,
						// a);

						// Util.printZBuffer("runningZed", runningZed);
						f++;
					}
					// timeEnd = System.currentTimeMillis();

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
		// System.out.println("time took to render: " + (timeEnd -
		// timeBegin));
		// System.out.println("currentThread: " + Thread.currentThread());
		// System.out.println("size of zBufer " + (zBuffer.length *
		// zBuffer[0].length));

		return;

	}

	public Pixel[][] renerPolygon(Panel p, BufferedImage frame, Face face, Point3D[] points,
			Double[][] mat, Pixel[][] zBuffer, Obj obj, int f) {
		Double[][] screenPoints = new Double[face.indices.length][2];
		Double avgZ = 0.0;

		for (int i = 0; i < face.indices.length; i++) {
			Double[][] affterCamPos = MathUtil.Mat.multi(points[face.getIndex(i)].getMat(), mat);

			screenPoints[i] = p.projectionA(affterCamPos[0]);

			avgZ += affterCamPos[0][2];
		}
		avgZ /= face.indices.length;

		Polygon poly = Util.Poly.fromDouble(screenPoints);
		// face.drawOutline = false;
		if (poly.contains(p.mh.m)) {
			obj.drawOutline = true;
			// face.drawOutline = true;
			// System.out.println("polgon:" + f);
		}

		double xFactor = 0;
		double yFactor = 0;
		double xDif = 0;
		double yDif = 0;
		int xRead = 0;
		int yRead = 0;
		// Double rotOffset = 0.0;
		Rectangle bounds = poly.getBounds();
		BufferedImage img = null;
		// Random ran = new Random();
		// double rand = ran.nextDouble() * 100 - 100;
		if (face.getImg() != null) {
			img = face.getImg();
			// xDif = MathUtil.diff(screenPoints[0][0], screenPoints[1][0]);
			// yDif = MathUtil.diff(screenPoints[0][1], screenPoints[1][1]);
			// int factor = (int) Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif,
			// 2));

			// xFactor = (double) (bounds.getWidth() * factor) / img.getWidth();
			// yFactor = (double) (bounds.getHeight() * factor) /
			// img.getHeight();

			//
			// int XI = Util.getHighPoint(screenPoints, 1, -1);
			// int YI = Util.getHighPoint(screenPoints, 0, -1);
			//
			// double fax = 0.7 *
			// ((Math.toDegrees(Math.abs(p.viewObject.rotationVec[1])) % 45) /
			// 45);
			//
			// xFactor = (double) MathUtil.diff(screenPoints[1][0],
			// screenPoints[2][0])
			// / img.getWidth();
			// yFactor = (double) MathUtil.diff(screenPoints[1][1],
			// screenPoints[3][1])
			// / img.getHeight();

			// xFactor = (double) factor / img.getWidth();
			// yFactor = (double) factor / img.getHeight();

			int indexHighY = Util.getHighPoint(screenPoints, 1, -1);

			// int oppsiteIndex = Util.getHighPoint(screenPoints, 0, 1);
			//
			// int highX = Util.getHighPoint(screenPoints, 0, 1);
			// int lowX = Util.getHighPoint(screenPoints, 0, -1);
			//
			// int widthIndex = indexHighY + 1;
			// int heightIndex = indexHighY - 1;
			// if (heightIndex < 0) {
			// heightIndex = (screenPoints.length - 1) - Math.abs(heightIndex +
			// 1);
			// }
			// if (widthIndex > screenPoints.length - 1) {
			// widthIndex = 0;
			// }

			// if (oppsiteIndex >= screenPoints.length) {
			// oppsiteIndex = oppsiteIndex - screenPoints.length - 1;
			// }
			// if (oppsiteIndex <= 0) {
			// oppsiteIndex += screenPoints.length - 1;
			// }

			// System.out.println("oppsiteIndex:" + oppsiteIndex);
			// System.out.println("indexHighY:" + indexHighY);
			// System.out.println("widthIndex:" + widthIndex);
			// System.out.println("heightIndex:" + heightIndex);

			// Double avgLength = Util.avgVecLength(screenPoints);
			// System.out.println("avgLength:" + avgLength);

			// double newWidth = Util.vecLength(screenPoints[indexHighY],
			// screenPoints[widthIndex]);
			// double newHeight = Util.vecLength(screenPoints[indexHighY],
			// screenPoints[heightIndex]);
			// double newWidth = MathUtil.diff(screenPoints[heightIndex][0],
			// screenPoints[widthIndex][0]);
			// double newHeight = MathUtil.diff(screenPoints[widthIndex][1],
			// screenPoints[heightIndex][1]);

			// double newWidth = MathUtil.diff(screenPoints[0][0],
			// screenPoints[screenPoints.length / 2][0]);
			// double newHeight = MathUtil.diff(screenPoints[0][1],
			// screenPoints[screenPoints.length / 2][1]);;

			// double newWidth = MathUtil.diff(screenPoints[oppsiteIndex][0],
			// screenPoints[indexHighY][0]);
			// double newHeight = screenPoints[oppsiteIndex][1] -
			// screenPoints[indexHighY][1];

			// double rot = (Math.toDegrees(p.viewObject.rotationVec[1]) % 90) -
			// 90;
			//
			// rot /= 45;

			// System.out.println("rot:" + rot);

			// double newWidth = Util.vecLength(screenPoints[indexHighY],
			// screenPoints[highX]);
			// double newHeight = Util.vecLength(screenPoints[indexHighY],
			// screenPoints[lowX]);

			// double newWidth = Util.vecLength(screenPoints[0],
			// screenPoints[1]);
			// double newHeight = Util.vecLength(screenPoints[0],
			// screenPoints[screenPoints.length - 1]);
			//
			// xFactor = (double) (newWidth) / img.getWidth();
			// yFactor = (double) (newHeight) / img.getHeight();

			xRead = (int) Math.round(screenPoints[indexHighY][0]);
			yRead = (int) Math.round(screenPoints[indexHighY][1]);

			// double avgScreenSize = (p.size.width + p.size.height) / 2.0;
			// double avgImgSize = (img.getWidth() + img.getHeight()) / 2.0;
			//
			// double scaleFactor = avgScreenSize / avgImgSize;
			//
			// xFactor = scaleFactor;
			// yFactor = scaleFactor;

			// xFactor = (double) MathUtil.diff(screenPoints[XI][0],
			// screenPoints[XI + 1 >= screenPoints.length ? XI - 1 : XI + 1][0])
			// / img.getWidth();
			// yFactor = (double) MathUtil.diff(screenPoints[YI][1],
			// screenPoints[YI + 1 >= screenPoints.length ? YI - 1 : YI + 1][1])
			// / img.getHeight();
			// xFactor = (double) (bounds.getWidth() * fax) / img.getWidth();
			// yFactor = (double) (bounds.getHeight() * fax) / img.getHeight();
			// img = Util.rotateImageByDegrees(img, 45);
			// System.out.println("factor:" + factor);
			// System.out.println("xDif:" + xDif);
			// System.out.println("yDif:" + yDif);
			// xFactor = (double) (bounds.getWidth()
			// - (Math.toDegrees(p.viewObject.rotationVec[1] % 4)
			// % 90)) / img.getWidth();
			// yFactor = (double) (bounds.getHeight()
			// - (Math.toDegrees(p.viewObject.rotationVec[1] % 4) % 90)) /
			// img.getHeight();

			// xFactor = (double) (bounds.getWidth() * p.scaleTex) /
			// img.getWidth();
			// yFactor = (double) (bounds.getHeight() * p.scaleTex) /
			// img.getHeight();

			// xFactor /= p.scaleTex;
			// yFactor /= p.scaleTex;

			// xFactor = (double) (192) / (img.getWidth());
			// yFactor = (double) (192) / (img.getHeight());

			xFactor = (double) (bounds.getWidth()) / img.getWidth();
			yFactor = (double) (bounds.getHeight()) / img.getHeight();
			// System.out.println("newWidth:" + newWidth);
			// System.out.println("bounds width:" + bounds.getWidth());
			// System.out.println("newHeight:" + newHeight);

			// System.out.println("bounds height:" + bounds.getHeight());
			// System.out.println("bounds ratio:" + bounds.getWidth() /
			// bounds.getHeight());
			// System.out.println("new ratio:" + newWidth / newHeight);
			// System.out.println("scaleTex:" + p.scaleTex);

			// rotOffset = (Math.toDegrees(p.viewObject.rotationVec[1]) % 90) -
			// 45;
			// rotOffset /= 45;
			//
			// rotOffset = Math.abs(rotOffset) * -1 + 1;
			// System.out.println("rotOffset:" + rotOffset);
			// System.out.println("---------------------------");
		}

		int[][] filledPixels = Util.Poly.fillPolyArray(poly, p.size.width, p.size.height);

		for (int x = 0; x < filledPixels.length; x++) {
			for (int y = 0; y < filledPixels[0].length; y++) {
				if (filledPixels[x][y] == 1 && (zBuffer[x][y].zedBuffer > avgZ)) {
					if (img != null) {

						// gets the uv cords relitive
						// int getX = (int) (((x - xRead)
						// - ((Conts.TEXTURE_WIGGLE + p.scaleTex) * rotOffset))
						// / xFactor);
						// int getY = (int) (((y - yRead)
						// - ((Conts.TEXTURE_WIGGLE + p.scaleTex) * rotOffset))
						// / yFactor);
						int getX = (int) (((x - xRead)) / xFactor);
						int getY = (int) (((y - yRead)) / yFactor);

						// makes vector to roate
						Double[] vec = {(double) getX, (double) getY};

						// translate vecotor to center
						// vec[0] -= (img.getWidth()) / 2;
						// vec[1] -= (img.getHeight()) / 2;
						// rotaes vector
						vec = MathUtil.vec3.rot2x2(vec,
								Math.toDegrees(p.viewObject.rotationVec[1]) + face.imgRot);
						// transltes vector back to origin
						// vec[0] += (img.getWidth()) / 2;
						// vec[1] += (img.getHeight()) / 2;

						// translates vector for propper rotation about 0,0
						if (vec[0] < 0) {
							vec[0] = vec[0] + img.getWidth();
						}
						if (vec[1] < 0) {
							vec[1] = vec[1] + img.getHeight();
						}
						// System.out.println("x:" + vec[0] + " y:" + vec[1]);

						getX = (int) vec[0].doubleValue();
						getY = (int) vec[1].doubleValue();

						// protects again bad vector cords
						getX = (int) Math.max(0, getX);
						getY = (int) Math.max(0, getY);

						getX = (int) Math.min(img.getWidth() - 1, getX);
						getY = (int) Math.min(img.getHeight() - 1, getY);
						//
						// System.out.println("x:" + getX + " y:" + getY);

						// getY = Math.max(getY, 0);
						// System.out.println("x:" + getX + ", y:" + getY);
						// (-getX) + face.getImg().getWidth() - 1
						// (-getY) + face.getImg().getHeight() - 1)
						zBuffer[x][y] = new Pixel(avgZ, new Color(img.getRGB(getX, getY)));
					} else {
						zBuffer[x][y] = new Pixel(avgZ, face.getColor());
					}
				}
			}
		}
		return zBuffer;

	}
	public void renderWireFrame(Panel p, Obj obj, Graphics2D g2d, Double[][] mat) {
		// for (int i = 0; i < edges.length; i++) {
		// Point3D startPoint = obj.points[edges[i].start];
		// Point3D endPoint = obj.points[edges[i].end];
		// // MathUtil.Mat.print("for loop: ", mat);
		// startPoint = Struct.toPoint3D(MathUtil.Mat.multi(startPoint.getMat(),
		// mat));
		// endPoint = Struct.toPoint3D(MathUtil.Mat.multi(endPoint.getMat(),
		// mat));
		//
		// Point2D start = p.projectionA(startPoint);
		// Point2D end = p.projectionA(endPoint);
		//
		// g2d.setColor(Color.MAGENTA);
		//
		// g2d.drawLine((int) start.x.doubleValue(), (int)
		// start.y.doubleValue(), // Comment
		// (int) end.x.doubleValue(), (int) end.y.doubleValue());
		//
		// }

		for (int i = 0; i < obj.faces.length; i++) {

			// if (obj.faces[i].drawOutline) {
			// System.out.println("draw frame:" + i);
			// }
			obj.faces[i].drawOutline = false;
			for (int j = 1; j < obj.faces[i].indices.length; j++) {
				Double[][] Point1 = MathUtil.Mat
						.multi(obj.points[obj.faces[i].getIndex(j)].getMat(), mat);
				Double[][] Point2 = MathUtil.Mat
						.multi(obj.points[obj.faces[i].getIndex(j - 1)].getMat(), mat);

				Double[] p1 = p.projectionA(Point1[0]);
				Double[] p2 = p.projectionA(Point2[0]);

				g2d.setColor(Color.black);
				// g2d.setStroke(new BasicStroke(2));
				g2d.drawLine(Util.round(p1[0]), Util.round(p1[1]), Util.round(p2[0]),
						Util.round(p2[1]));

			}
			Double[][] Point1 = MathUtil.Mat.multi(obj.points[obj.faces[i].getIndex(0)].getMat(),
					mat);
			Double[][] Point2 = MathUtil.Mat.multi(
					obj.points[obj.faces[i].getIndex(obj.faces[i].indices.length - 1)].getMat(),
					mat);

			Double[] p1 = p.projectionA(Point1[0]);
			Double[] p2 = p.projectionA(Point2[0]);

			g2d.setColor(Color.black);
			// g2d.setStroke(new BasicStroke(2));
			g2d.drawLine(Util.round(p1[0]), Util.round(p1[1]), Util.round(p2[0]),
					Util.round(p2[1]));
		}
		// g2d.setStroke(new BasicStroke(1));
	}

}
