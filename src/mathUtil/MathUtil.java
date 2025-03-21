package mathUtil;

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
	public static Double pCord(Double cord, Double FOV, Double z) {
		return (cord * FOV) / (z + FOV);
	}
}
