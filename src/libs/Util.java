package libs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import libs.Struct.Face;
import libs.Struct.Pixel;
import libs.Struct.Point3D;

public class Util {
	public static class Poly {
		public static int[][] fillPolyArray(Polygon poly, int maxWidth, int maxHeight) {
			Rectangle getBounds = poly.getBounds();
			int width = (int) getBounds.getWidth();
			int height = (int) getBounds.getHeight();

			int startX = Math.max(0, getBounds.x);
			int startY = Math.max(0, getBounds.y);

			int endX = Math.min(getBounds.x + width, maxWidth);
			int endY = Math.min(getBounds.y + height, maxHeight);

			if (endX <= startX || endY <= startY) {
				return new int[][]{{0}};
			}

			int[][] out = new int[endX][endY];

			// System.out.println("polyArr width: " + width);

			for (int x = startX; x < endX; x++) {
				for (int y = startY; y < endY; y++) {
					if (poly.contains(x, y)) {
						out[x][y] = 1;
					}
				}
			}
			return out;
		}
		public static Polygon fromDouble(Double[][] array) {
			Polygon out = new Polygon();
			// int i = 0;
			for (int i = 0; i < array.length; i++) {
				// System.out.println("fromDouble " + i + " : " + array[i][0]);
				out.addPoint((int) Math.round(array[i][0]), (int) Math.round(array[i][1]));
				// i++;
			}
			return out;
		}
		public static Polygon fromInt(int[][] array) {
			Polygon out = new Polygon();
			for (int[] point : array) {
				out.addPoint(point[0], point[1]);
			}
			return out;
		}
		public static Face[] getPolys(Face[] faces) {
			ArrayList<Face> out = new ArrayList<>();

			for (Face face : faces) {
				if (face.indices.length > 3) {
					out.add(face);
				}
			}
			Face[] ret = new Face[out.size()];
			for (int i = 0; i < out.size(); i++) {
				ret[i] = out.get(i);
			}
			// ret =
			return ret;
		}
		public static Face[] getTri(Face[] faces) {
			ArrayList<Face> out = new ArrayList<>();

			for (Face face : faces) {
				if (face.indices.length == 3) {
					out.add(face);
				}
			}
			Face[] ret = new Face[out.size()];
			for (int i = 0; i < out.size(); i++) {
				ret[i] = out.get(i);
			}
			// ret =
			return ret;
		}
	}

	public static Pixel[][] popArray2Dzb(Pixel filler, int length, int width) {
		Pixel[][] out = new Pixel[length][width];

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < width; j++) {
				out[i][j] = filler;
			}
		}

		return out;

	}
	public static void printZBuffer(String name, Pixel[][] zb) {
		System.out.print(name + ": ");
		for (Pixel[] array : zb) {
			for (Pixel p : array) {
				System.out.print(p.zedBuffer + " ");
			}
			System.out.println();
		}
	}
	public static Pixel[][] comparePixelArray(Pixel[][] a, Pixel[][] b) {

		if (a.length != b.length) {
			throw new IllegalArgumentException(
					"the size of a(" + a.length + ") and b(" + b.length + " must be the same!");
		}
		if (a[0].length != b[0].length) {
			throw new IllegalArgumentException("the size of a(" + a[0].length + ") and b("
					+ b[0].length + " must be the same! p.s. ooohhh boy. good luck with this one");
		}

		Pixel[][] c = new Pixel[a.length][a[0].length];

		for (int x = 0; x < a.length; x++) {
			for (int y = 0; y < b[0].length; y++) {
				c[x][y] = a[x][y].zedBuffer < b[x][y].zedBuffer ? a[x][y] : b[x][y];
			}
		}

		return c;
	}

	public static Rectangle getBounds(Point3D[] points) {
		int x = (int) points[0].x.doubleValue();
		int y = (int) points[0].y.doubleValue();
		int maxX = 0;
		int maxY = 0;

		for (int i = 0; i < points.length; i++) {
			if (points[i].x < x) {
				x = (int) points[i].x.doubleValue();
			}
			if (points[i].y < y) {
				y = (int) points[i].y.doubleValue();
			}
			if (points[i].x > maxX) {
				maxX = (int) points[i].x.doubleValue();
			}
			if (points[i].y > maxY) {
				maxY = (int) points[i].y.doubleValue();
			}

		}

		return new Rectangle(x, y, maxX - x, maxY - y);

	}
	public static double[] min(Double[][] points) {
		double minx = points[0][0];
		double miny = points[0][1];

		for (Double[] point : points) {
			if (point[0] < minx) {
				minx = point[0];
			}
			if (point[1] < miny) {
				miny = point[1];
			}
		}
		return new double[]{minx, miny};
	}

	public static Double vecLength(Double[] a, Double[] b) {
		double xLen = a[0] - b[0];
		double yLen = a[1] - b[1];

		Double vecLen = Math.sqrt((xLen * xLen) + (yLen * yLen));

		return vecLen;
	}
	public static Double avgVecLength(Double[][] mass) {
		assert mass.length > 0 : "main payload(mass) cant be nothing";
		assert mass[0].length == 2 : "main payloiad(mass) only does n by 2 lengths";
		assert mass[0][0] != null : "main payload(mass) cant be null";

		Double total = 0.0;

		for (int i = 1; i < mass.length; i++) {
			total += vecLength(mass[i], mass[i - 1]);
		}

		total /= mass.length;

		return total;
	}
	public static int getHighPoint(Double[][] mass, int indexCheck, int level) {
		assert level == -1 || level == 1
				: "you must choose a level high or lowel by setting level to 1 or -1 respectily!";
		assert mass != null : "main payload(mass) cant be null!";
		assert mass[0].length - 1 < indexCheck
				: "the index to check must be in the main payload (mass)";

		int index = 0;
		Double running = mass[index][indexCheck];

		for (int i = 0; i < mass.length; i++) {
			if (level == 1) {
				if (mass[i][indexCheck] > running) {
					running = mass[i][indexCheck];
					index = i;
				}
			} else if (level == -1) {
				if (mass[i][indexCheck] < running) {
					running = mass[i][indexCheck];
					index = i;
				}
			}
		}

		return index;
	}
	public static BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
		double rads = Math.toRadians(angle);
		double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
		int w = img.getWidth();
		int h = img.getHeight();
		int newWidth = (int) Math.floor(w * cos + h * sin);
		int newHeight = (int) Math.floor(h * cos + w * sin);

		BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = rotated.createGraphics();
		AffineTransform at = new AffineTransform();
		at.translate((newWidth - w) / 2, (newHeight - h) / 2);

		int x = w / 2;
		int y = h / 2;

		at.rotate(rads, x, y);
		g2d.setTransform(at);
		g2d.drawImage(img, 0, 0, null);
		g2d.setColor(Color.RED);
		g2d.drawRect(0, 0, newWidth - 1, newHeight - 1);
		g2d.dispose();

		return rotated;
	}
	public static class Tex {
		public static Color uvChecker(Double u, Double v, Color a, Color b) {

			return null;
		}
	}
	public static int round(Double a) {
		return (int) Math.round(a);
	}
}
