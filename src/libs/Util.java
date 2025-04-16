package libs;

import java.awt.Polygon;
import java.util.ArrayList;

import libs.Struct.Face;
import libs.Struct.Pixel;

public class Util {
	public static class Poly {
		public static int[][] fillPolyArray(Polygon poly, int maxWidth, int maxHeight) {
			int width = (int) Math.round(poly.getBounds().getWidth());
			int height = (int) Math.round(poly.getBounds().getHeight());

			int startX = poly.getBounds().x;

			width += startX;
			if (width < 0) {
				width = 0;
			}

			int startY = poly.getBounds().y;

			height += startY;
			if (height < 0) {
				height = 0;
			}

			width = width > maxWidth ? maxWidth : width;
			height = height > maxHeight ? maxHeight : height;

			int[][] out = new int[width][height];

			if (width <= 0 || height <= 0) {
				return new int[][]{{0}};
			}

			// System.out.println("polyArr width: " + width);

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					out[x][y] = (poly.contains(x, y)) ? 1 : 0;
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
}
