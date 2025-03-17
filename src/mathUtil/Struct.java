package mathUtil;

public class Struct {

	public static class Point3D {
		public Double x, y, z;

		public Point3D(Double x, Double y, Double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		public Double[][] getMat() {
			return new Double[][]{{x, y, z}};
		}

	}
	public static class Point2D {
		public Double x, y;
		public Point2D(Double x, Double y) {
			this.x = x;
			this.y = y;
		}
	}
	public static class Edge {
		public int start, end;
		public Edge(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}
	public static Point3D toPoint3D(Double[][] mat) {
		return new Point3D(mat[0][0], mat[0][1], mat[0][2]);
	}

}
