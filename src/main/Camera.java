package main;
import mathUtil.MathUtil;
public class Camera {

	public Double[] pos, angle;
	private Double[][] projectionMat4, viewMat4;

	public Camera(Double[] pos, Double[] angle) {
		this.pos = pos;
		this.angle = angle;
		projectionMat4 = MathUtil.Mat.getIdentity();
		viewMat4 = MathUtil.Mat.getIdentity();
	}
	public Camera() {
		this.pos = new Double[]{0.0, 0.0, 0.0};
		this.angle = new Double[]{0.0, 0.0, 0.0};
		projectionMat4 = MathUtil.Mat.getIdentity();
		viewMat4 = MathUtil.Mat.getIdentity();
	}
	// public void scalePos(Double scale) {
	// pos.x *= scale;
	// pos.y *= scale;
	// pos.z *= scale;
	// }
	// public Point3D scaledPos(Double scale) {
	// return new Point3D(pos.x * scale, pos.y * scale, pos.z * scale);
	// }
	// public void moveX(Double amout) {
	// pos.x += amout;
	// }
	// public void moveY(Double amout) {
	// pos.y += amout;
	// }
	// public void moveZ(Double amout) {
	// pos.z += amout;
	// }

	public void orthographicProjection(Double left, Double right, Double top, Double bottom,
			Double near, Double far) {
		projectionMat4 = MathUtil.Mat.getOrthographicProjection(left, right, top, bottom, near,
				far);
	}
	public void perspectiveProjection(Double fovy, Double aspect, Double near, Double far) {
		projectionMat4 = MathUtil.Mat.getPerspectiveProjection(fovy, aspect, near, far);
	}
	public Double[][] getProjection() {
		return projectionMat4;
	}
	public Double[][] getView() {
		return viewMat4;
	}
	public void setViewDirection(Double[] position, Double[] direction, Double[] up) {
		if (up == null) {
			up = new Double[]{0.0, -1.0, 0.0};
		}
		final Double[] w = MathUtil.vec3.normalize(direction);
		final Double[] u = MathUtil.vec3.normalize(MathUtil.vec3.cross(w, up));
		final Double[] v = MathUtil.vec3.cross(w, u);

		viewMat4 = MathUtil.Mat.getIdentity();
		viewMat4[0][0] = u[0];
		viewMat4[1][0] = u[1];
		viewMat4[2][0] = u[2];
		viewMat4[0][1] = v[0];
		viewMat4[1][1] = v[1];
		viewMat4[2][1] = v[2];
		viewMat4[0][2] = w[0];
		viewMat4[1][2] = w[1];
		viewMat4[2][2] = w[2];
		viewMat4[3][0] = MathUtil.dot(u, position, -1);
		viewMat4[3][1] = MathUtil.dot(v, position, -1);
		viewMat4[3][2] = MathUtil.dot(w, position, -1);
	}
	public void setViewTarget(Double[] position, Double[] target, Double[] up) {
		if (up == null) {
			up = new Double[]{0.0, -1.0, 0.0};
		}
		setViewDirection(position, MathUtil.vec3.sub(target, position), up);

	}
	public void setViewXYZ(Double[] position, Double[] rotation) {
		final Double c3 = Math.cos(rotation[2]);
		final Double s3 = Math.sin(rotation[2]);
		final Double c2 = Math.cos(rotation[0]);
		final Double s2 = Math.sin(rotation[0]);
		final Double c1 = Math.cos(rotation[1]);
		final Double s1 = Math.sin(rotation[1]);
		final Double[] u = {(c1 * c3 + s1 * s2 * s3), (c2 * s3), (c1 * s2 * s3 - c3 * s1)};
		final Double[] v = {(c3 * s1 * s2 - c1 * s3), (c2 * c3), (c1 * c3 * s2 + s1 * s3)};
		final Double[] w = {(c2 * s1), (-s2), (c1 * c2)};
		viewMat4 = MathUtil.Mat.getIdentity();
		viewMat4[0][0] = u[0];
		viewMat4[1][0] = u[1];
		viewMat4[2][0] = u[2];
		viewMat4[0][1] = v[0];
		viewMat4[1][1] = v[1];
		viewMat4[2][1] = v[2];
		viewMat4[0][2] = w[0];
		viewMat4[1][2] = w[1];
		viewMat4[2][2] = w[2];
		viewMat4[3][0] = MathUtil.dot(u, position, -1);
		viewMat4[3][1] = MathUtil.dot(v, position, -1);
		viewMat4[3][2] = MathUtil.dot(w, position, -1);
	}

}
