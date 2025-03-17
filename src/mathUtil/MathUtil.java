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
						C[i][j] += A[i][k] * B[k][j];
					}
				}
			}

			return C;
		}

		public static Double[][] rotY(Double[][] A, double angle) {
			Double[][] rotY = {{Math.cos(angle), 0.0, Math.sin(angle)}, {0.0, 1.0, 0.0},
					{-Math.sin(angle), 0.0, Math.cos(angle)}};

			return multi(A, rotY);
		}
		public static Double[][] rotX(Double[][] A, double angle) {
			Double[][] rotX = {{1.0, 0.0, 0.0}, {0.0, Math.cos(angle), -Math.sin(angle)},
					{0.0, Math.sin(angle), Math.cos(angle)}};

			return multi(A, rotX);
		}
		public static Double[][] rotZ(Double[][] A, double angle) {
			Double[][] rotZ = {{Math.cos(angle), -Math.sin(angle), 0.0},
					{Math.sin(angle), Math.cos(angle), 0.0}, {0.0, 0.0, 1.0}};

			return multi(A, rotZ);
		}
	}
	public static Double pCord(Double cord, Double FOV, Double z) {
		return (cord * FOV) / (z + FOV);
	}
}
