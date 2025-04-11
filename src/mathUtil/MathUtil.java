package mathUtil;

import mathUtil.Struct.Point2D;

public class MathUtil {

	public class Mat {

		public static Double[][] multi(Double[][] A, Double[][] B) {

			int aRows = A.length;
			int aColumns = A[0].length;
			int bRows = B.length;
			int bColumns = B[0].length;

			if (aColumns != bRows) {
				throw new IllegalArgumentException(
						"A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
			}

			Double[][] C = new Double[aRows][bColumns];
			for (int i = 0; i < aRows; i++) {
				for (int j = 0; j < bColumns; j++) {
					C[i][j] = 0.00000;
				}
			}

			for (int i = 0; i < aRows; i++) { // aRow
				for (int j = 0; j < bColumns; j++) { // bColumn
					for (int k = 0; k < aColumns; k++) { // aColumn
						// System.out.println("i: " + i);
						// System.out.println("j: " + j);
						// System.out.println("k: " + k);
						C[i][j] += A[i][k] * B[k][j];
					}
				}
			}

			return C;
		}
		public static Double[][] sub(Double[][] A, Double[][] B) {
			int aRows = A.length;
			int aColumns = A[0].length;
			int bRows = B.length;
			int bColumns = B[0].length;

			if (aColumns != bColumns) {
				throw new IllegalArgumentException(
						"A:Columns: " + aColumns + " did not match B:Columns " + bColumns + ".");
			}
			if (aRows != bRows) {
				throw new IllegalArgumentException(
						"A:Rows: " + aRows + " did not match B:Rows " + bRows + ".");
			}
			Double[][] C = new Double[aRows][aColumns];
			for (int i = 0; i < aRows; i++) {
				for (int j = 0; j < aColumns; j++) {
					C[i][j] = A[i][j] - B[i][j];
				}
			}
			return C;
		}
		public static Double[][] clip(Double[][] transform, Double width, Double height,
				Double farPlane, Double nearPlane, Double theta) {
			System.out.println("transform length: " + transform[0].length);
			return multi(transform,
					getPersectiveProjectionMat(width, height, farPlane, nearPlane, theta));
		}

		public static Double[][] getPersectiveProjectionMat(Double w, Double h, Double f, Double n,
				Double theta) {
			Double[][] persectiveProjectionMatrix = {
					{1 / ((w / h) * Math.tan(theta / 2)), 0.0, 0.0, 0.0},
					{0.0, 1 / (Math.tan(theta / 2)), 0.0, 0.0}, {0.0, 0.0, f / (f - n), 1.0},
					{0.0, 0.0, -f * n / (f - n), 0.0}};

			return persectiveProjectionMatrix;
		}
		public static Double[][] getOrthographicProjection(Double left, Double right, Double top,
				Double bottom, Double near, Double far) {
			Double[][] projectionMatrix = getIdentity();
			projectionMatrix[0][0] = 2.f / (right - left);
			projectionMatrix[1][1] = 2.f / (bottom - top);
			projectionMatrix[2][2] = 1.f / (far - near);
			projectionMatrix[3][0] = -(right + left) / (right - left);
			projectionMatrix[3][1] = -(bottom + top) / (bottom - top);
			projectionMatrix[3][2] = -near / (far - near);
			return projectionMatrix;
		}

		public static Double[][] getPerspectiveProjection(Double fovy, Double aspect, Double near,
				Double far) {
			assert Math.abs(aspect - Float.MIN_VALUE) > 0.0f;

			final Double tanHalfFovy = Math.tan(fovy / 2.f);
			Double[][] projectionMatrix = getIdentity();
			projectionMatrix[0][0] = 1.0 / (aspect * tanHalfFovy);
			projectionMatrix[1][1] = 1.0 / (tanHalfFovy);
			projectionMatrix[2][2] = far / (far - near);
			projectionMatrix[2][3] = 1.0;
			projectionMatrix[3][2] = -(far * near) / (far - near);
			return projectionMatrix;
		}
		public static Double[][] translate(Double[][] A, Double[] vec) {
			Double[][] trans = {{1.0, 0.0, 0.0, 0.0}, {0.0, 1.0, 0.0, 0.0}, {0.0, 0.0, 1.0, 0.0},
					{vec[0], vec[1], vec[2], 1.0}};
			return multi(A, trans);
		}
		// public static Double[][] scale3D(Double[][] transform, Double[][]
		// scale) {
		// Double[][] scaleMat = {{scale[0][0], 0.0, 0.0, 0.0}, {0.0,
		// scale[0][1], 0.0, 0.0},
		// {0.0, 0.0, scale[0][2], 0.0}, {0.0, 0.0, 0.0, 1.0}};
		// return multi(transform, scale);
		// }
		public static Double[][] scale(Double[][] mat, Double[] scaleVector) {
			Double[][] scaleMatrix = {{scaleVector[0], 0.0, 0.0, 0.0},
					{0.0, scaleVector[1], 0.0, 0.0}, {0.0, 0.0, scaleVector[2], 0.0},
					{0.0, 0.0, 0.0, 1.0}};

			return multi(scaleMatrix, mat);
		}
		// public static Double[][] translation(Double[][] transform, Double[][]
		// translation){
		//
		// }
		public static Double[] getVector4x4(Double[][] mat) {
			return new Double[]{mat[0][0], mat[1][1], mat[2][2], mat[3][3]};
		}
		public static Double[][] rotX4x4(Double[] A, Double theta) {
			Double[][] rot = {{1.0, 0.0, 0.0, 0.0}, {0.0, Math.cos(theta), -Math.sin(theta), 0.0},
					{0.0, Math.sin(theta), Math.cos(theta), 0.0}, {0.0, 0.0, 0.0, 1.0}};
			Double[][] mat = new Double[][]{A};
			return multi(rot, mat);
		}
		public static Double[][] rotY4x4(Double[] A, Double theta) {
			Double[][] rot = {{Math.cos(theta), 0.0, Math.sin(theta), 0.0}, {0.0, 1.0, 0.0, 0.0},
					{-Math.sin(theta), 0.0, Math.cos(theta), 0.0}, {0.0, 0.0, 0.0, 1.0}};
			Double[][] mat = {{A[0], A[1], A[2], 1.0}};
			return multi(mat, rot);
		}
		public static Double[][] rotZ4x4(Double[] A, Double theta) {
			Double[][] rot = {{Math.cos(theta), -Math.sin(theta), 0.0, 0.0},
					{Math.sin(theta), Math.cos(theta), 0.0, 0.0}, {0.0, 0.0, 1.0, 0.0},
					{0.0, 0.0, 0.0, 1.0}};
			Double[][] mat = {A};
			return multi(rot, mat);
		}
		// 3x3
		public static Double[][] rotY3x3(Double[][] A, double angle) {
			Double[][] rotY = {{Math.cos(angle), 0.0, Math.sin(angle)}, {0.0, 1.0, 0.0},
					{-Math.sin(angle), 0.0, Math.cos(angle)}};

			return multi(A, rotY);
		}
		public static Double[][] rotX3x3(Double[][] A, double angle) {
			Double[][] rotX = {{1.0, 0.0, 0.0}, {0.0, Math.cos(angle), -Math.sin(angle)},
					{0.0, Math.sin(angle), Math.cos(angle)}};

			return multi(A, rotX);
		}
		public static Double[][] rotZ3x3(Double[][] A, double angle) {
			Double[][] rotZ = {{Math.cos(angle), -Math.sin(angle), 0.0},
					{Math.sin(angle), Math.cos(angle), 0.0}, {0.0, 0.0, 1.0}};

			return multi(A, rotZ);
		}
		public static Double[][] getIdentity() {
			return new Double[][]{{1.0, 0.0, 0.0, 0.0}, {0.0, 1.0, 0.0, 0.0}, {0.0, 0.0, 1.0, 0.0},
					{0.0, 0.0, 0.0, 1.0}};
		}
		public static Double mod(Double x, Double y) {
			return x - y * Math.floor(x / y);
		}
		public static void print(Double[][] mat) {
			if (mat != null) {
				int columns = mat.length;
				int rows = mat[0].length;

				for (int i = 0; i < rows; i++) {
					System.out.print("[ ");
					for (int k = 0; k < columns; k++) {
						System.out.print(mat[k][i] + ", ");
					}
					System.out.print("]");
					System.out.println();
				}
				System.out.println();
			}
		}
		public static void print(String name, Double[][] mat) {
			System.out.print(name);
			if (mat != null) {
				int columns = mat.length;
				int rows = mat[0].length;

				for (int i = 0; i < rows; i++) {
					System.out.print("[ ");
					for (int k = 0; k < columns; k++) {
						System.out.print(mat[k][i] + ", ");
					}
					System.out.print("]");
					System.out.println();
				}
				System.out.println();
			}
		}
	}
	public static class tri {
		// Function to compute the area of a triangle using the determinant
		// method
		static double triangleArea(Point2D p1, Point2D p2, Point2D p3) {
			return 0.5
					* Math.abs(p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y));
		}

		// Function to check if a point (px, py) lies inside the triangle ABC
		// using barycentric coordinates
		static boolean isPointInsideTriangle(Point2D A, Point2D B, Point2D C, double px,
				double py) {
			// Calculate area of the full triangle
			double areaABC = triangleArea(A, B, C);

			// Calculate areas of sub-triangles formed with the point (px, py)
			double areaPAB = triangleArea(new Point2D(px, py), A, B);
			double areaPBC = triangleArea(new Point2D(px, py), B, C);
			double areaPCA = triangleArea(new Point2D(px, py), C, A);

			// If sum of sub-areas is same as the area of the triangle, the
			// point lies inside or on the edge
			return areaABC - (areaPAB + areaPBC + areaPCA) < .05
					&& areaABC - (areaPAB + areaPBC + areaPCA) > -.05;
		}

		// Function to compute all pixels within the triangle and return them as
		// a 2D int array
		public static int[][] getTrianglePixels(Point2D A, Point2D B, Point2D C, int width,
				int height) {
			// Initialize the result array to store the pixel values (1 for
			// inside, 0 for outside)

			if (width <= 0 || height <= 0) {
				return new int[][]{{0}};
			}
			int[][] pixels = new int[width][height];

			// Get the bounding box of the triangle
			int minX = (int) Math.min(A.x, Math.min(B.x, C.x));
			int minY = (int) Math.min(A.y, Math.min(B.y, C.y));
			int maxX = (int) Math.max(A.x, Math.max(B.x, C.x));
			int maxY = (int) Math.max(A.y, Math.max(B.y, C.y));

			// Iterate through all points in the bounding box
			for (int x = minX; x < maxX; x++) {
				for (int y = minY; y < maxY; y++) {
					// Check if the point (x, y) is inside the triangle
					if (x >= 0 && x < width && y >= 0 && y < height) { // Ensure
																		// within
																		// bounds
						if (isPointInsideTriangle(A, B, C, x, y)) {
							// Mark the pixel as inside the triangle (1 for
							// inside)
							pixels[x][y] = 1;
						} else {
							// Mark the pixel as outside the triangle (0 for
							// outside)

							pixels[x][y] = 0;

						}
					}
				}
			}

			return pixels;
		}
	}
	public static class vec3 {
		public static Double[] normalize(Double[] vec3) {

			Double length_of_v = Math
					.sqrt((vec3[0] * vec3[0]) + (vec3[1] * vec3[1]) + (vec3[2] * vec3[2]));
			if (Math.abs(length_of_v) == 0) {
				throw new IllegalArgumentException("vec3 cant be zero!");
			}
			return new Double[]{vec3[0] / length_of_v, vec3[1] / length_of_v,
					vec3[2] / length_of_v};
		}
		public static Double[] cross(Double[] x, Double[] y) {
			return new Double[]{// comment
					x[1] * y[2] - y[1] * x[2]// commnet
					, x[2] * y[0] - y[2] * x[0], // commnet
					x[0] * y[1] - y[0] * x[1]};
		}
		public static Double[] sub(Double[] a, Double[] b) {
			return new Double[]{a[0] - b[0], a[1] - b[1], a[2] - b[2]};
		}
		public static Double[] add(Double[] a, Double[] b) {
			return new Double[]{a[0] + b[0], a[1] + b[1], a[2] + b[2]};
		}
		public static Double[] scale(Double[] a, Double factor) {
			assert factor != 0 : "factor can be zero but you dont get a vector!";
			return new Double[]{a[0] * factor, a[1] * factor, a[2] * factor};
		}
	}
	public static Double dot(Double[] x, Double[] y, int sign) {
		assert sign == 1 || sign == 0 : "sign can only be 1 or -1";
		if (x.length != y.length) {
			throw new IllegalArgumentException(
					"x length(" + x.length + ") and y length(" + y.length + ") must be equal");
		}
		Double out = 0.0;
		for (int i = 0; i < x.length; i++) {
			out += (x[i] * y[i]) * sign;
		}
		return out;
	}
	public static Double pCord(Double cord, Double FOV, Double z) {
		return (cord * FOV) / (z + FOV);
	}
	// Method to initialize a 2D array to a given value
	// public static void initialize2DArray(Pixel[][] array, int value) {
	// for (int i = 0; i < array.length; i++) {
	// for (int j = 0; j < array[i].length; j++) {
	// array[i][j].zedBuffer = value; // Set each element to the given
	// // value
	// }
	// }
	// }

	// Method to print a 2D array for visualization
	public static void print2DArray(int[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				System.out.print(array[i][j] + " ");
			}
			System.out.println();
		}
	}
	public static void print2DArray(String name, int[][] array) {
		System.out.println(name + ": ");
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				System.out.print(array[i][j] + " ");
			}
			System.out.println();
		}
	}
}
