package libs;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Struct {
	public static class Obj {
		public Double[][] scale, transformMat, mat, toCamSpaceMat;
		public Double[] translationVec, scaleVec, rotationVec;
		public Point3D[] points;
		public Face[] faces;
		public boolean hoverOver, focused = false;
		// public Double rotaion = 0.0;

		public Obj(Point3D[] points) {
			translationVec = new Double[]{1.0, 1.0, 1.0};
			rotationVec = new Double[]{1.0, 1.0, 1.0};
			scaleVec = new Double[]{1.0, 1.0, 1.0};
			transformMat = MathUtil.Mat.getIdentity();

			this.points = points;
		}
		public Obj(Point3D[] points, Double[] translationVec, Double[] rotationVec,
				Double[] scaleVec) {
			this.translationVec = translationVec;
			this.rotationVec = rotationVec;
			this.scaleVec = scaleVec;
			transformMat = MathUtil.Mat.getIdentity();

			this.points = points;
		}
		public Obj(Point3D[] points, Face[] faces, Double[] translationVec, Double[] rotationVec,
				Double[] scaleVec) {
			this.translationVec = translationVec;
			this.rotationVec = rotationVec;
			this.scaleVec = scaleVec;
			this.faces = faces;
			transformMat = MathUtil.Mat.getIdentity();

			this.points = points;
		}
		public Obj() {
			translationVec = new Double[]{0.0, 0.0, 0.0};
			rotationVec = new Double[]{0.0, 0.0, 0.0};
			scaleVec = new Double[]{1.0, 1.0, 1.0};
			transformMat = MathUtil.Mat.getIdentity();
		}
		public Obj(Double[] transVec, Double[] rotaionVec) {
			translationVec = new Double[]{1.0, 1.0, 1.0};
			rotationVec = new Double[]{1.0, 1.0, 1.0};
			scaleVec = new Double[]{1.0, 1.0, 1.0};
			transformMat = MathUtil.Mat.getIdentity();
		}

		public Double[][] mat4() {
			final Double c3 = Math.cos(rotationVec[2]);
			final Double s3 = Math.sin(rotationVec[2]);
			final Double c2 = Math.cos(rotationVec[0]);
			final Double s2 = Math.sin(rotationVec[0]);
			final Double c1 = Math.cos(rotationVec[1]);
			final Double s1 = Math.sin(rotationVec[1]);
			Double[][] mat4 = {{// Comment
					scaleVec[0] * (c1 * c3 + s1 * s2 * s3), // Comment
					scaleVec[0] * (c2 * s3), // Comment
					scaleVec[0] * (c1 * s2 * s3 - c3 * s1), // Comment
					0.0,// Comment
					}, // Comment
					{// Comment
							scaleVec[1] * (c3 * s1 * s2 - c1 * s3), // Comment
							scaleVec[1] * (c2 * c3), // Comment
							scaleVec[1] * (c1 * c3 * s2 + s1 * s3), // Comment
							0.0,// Comment
					}, // Comment
					{// Comment
							scaleVec[2] * (c2 * s1), // Comment
							scaleVec[2] * (-s2), // Comment
							scaleVec[2] * (c1 * c2), // Comment
							0.0,// Comment
					}, // Comment
					{translationVec[0], translationVec[1], translationVec[2], 1.0}};// Comment
			return mat4;
		}// Comment
		public void rotate(Double x, Double y, Double z) {
			rotationVec = new Double[]{x, y, z};
		}
		public Double[] worldPoint(int index) {
			return MathUtil.Mat.multi(points[index].getMat(), mat4())[0];
		}
	}

	public static class Point3D {
		public Double x, y, z, w;

		public Point3D(Double x, Double y, Double z) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.w = 1.0;
		}
		public Point3D(Double x, Double y, Double z, Double w) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.w = w;
		}
		public Double[][] getMat() {
			return new Double[][]{{x, y, z, w}};
		}

		public Point3D sub(Point3D a) {
			return new Point3D(x - a.x, y - a.y, z - a.z);
			// return new Point3D(Struct.toPoint3D(MathUtil.Mat.sub(getMat(),
			// a.getMat())));
		}
		public Point3D normalize() {
			if (w != 0) {
				return new Point3D(x / w, y / w, z / w);
			}
			return this; // Return the point unchanged if w is 0 (avoid division
							// by zero)
		}
		public Double[] getPoint() {
			return new Double[]{x, y, z};
		}

	}
	public static class Point2D {
		public Double x, y, w;
		public Point2D(Double x, Double y) {
			this.x = x;
			this.y = y;
			this.w = 1.0;
		}
		public Point2D(Double x, Double y, Double w) {
			this.x = x;
			this.y = y;
			this.w = w;
		}
		public Double[] getVec2() {
			return new Double[]{x, y};
		}
	}
	public static class Edge {
		public int start, end;
		public Edge(int start, int end) {
			this.start = start;
			this.end = end;
		}

	}
	public static class Face {
		private Color color;
		public int[] indices;
		public boolean fill = true;
		public Double avgZ;
		public BufferedImage img = null;
		public int imgRot = 0;
		public boolean drawOutline = false;
		public Face(int[] indices) {
			this.indices = indices;
			color = Color.white;
		}
		public Face(int[] indices, Color color) {
			this.indices = indices;
			this.color = color;
		}
		public int getIndex(int index) {
			return indices[index];
		}
		public Color getColor() {
			return color;
		}
		public void setColor(Color color) {
			this.color = color;
		}
		public BufferedImage getImg() {
			return img;
		}
		public void setImg(BufferedImage img) {
			this.img = img;
		}
	}
	public static class Pixel {
		public Double zedBuffer = 10000.0;
		public Color color = Color.green;

		public Pixel(Double zedBuffer) {
			this.zedBuffer = zedBuffer;
			color = Color.yellow;
		}
		public Pixel(Double zedBuffer, Color color) {
			this.zedBuffer = zedBuffer;
			this.color = color;
		}
	}

	public static Point3D toPoint3D(Double[][] mat) {
		return new Point3D(mat[0][0], mat[0][1], mat[0][2]);
	}
	public static int[] toPointsX(Point2D[] array) {
		return new int[]{(int) array[0].x.doubleValue(), (int) array[1].x.doubleValue(),
				(int) array[2].x.doubleValue()};
	}
	public static int[] toPointsX(Point2D a, Point2D b, Point2D c) {
		Point2D[] array = {a, b, c};
		return new int[]{(int) array[0].x.doubleValue(), (int) array[1].x.doubleValue(),
				(int) array[2].x.doubleValue()};
	}
	public static int[] toPointsY(Point2D[] array) {
		return new int[]{(int) array[0].y.doubleValue(), (int) array[1].y.doubleValue(),
				(int) array[2].y.doubleValue()};
	}
	public static int[] toPointsY(Point2D a, Point2D b, Point2D c) {
		Point2D[] array = {a, b, c};
		return new int[]{(int) array[0].y.doubleValue(), (int) array[1].y.doubleValue(),
				(int) array[2].y.doubleValue()};
	}

}
