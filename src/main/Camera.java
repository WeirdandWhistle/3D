package main;
import mathUtil.MathUtil;
import mathUtil.Struct.Point3D;
public class Camera {

	public Point3D pos, angle;
	private Double[][] mat4;

	public Camera(Point3D pos, Point3D angle) {
		this.pos = pos;
		this.angle = angle;
		mat4 = MathUtil.Mat.getIdentity();
	}
	public void scalePos(Double scale) {
		pos.x *= scale;
		pos.y *= scale;
		pos.z *= scale;
	}
	public Point3D scaledPos(Double scale) {
		return new Point3D(pos.x * scale, pos.y * scale, pos.z * scale);
	}
	public void moveX(Double amout) {
		pos.x += amout;
	}
	public void moveY(Double amout) {
		pos.y += amout;
	}
	public void moveZ(Double amout) {
		pos.z += amout;
	}
	public void orthographicProjection(Double left, Double right, Double top, Double bottom,
			Double near, Double far) {
		mat4 = MathUtil.Mat.getOrthographicProjection(left, right, top, bottom, near, far);
	}
	public void perspectiveProjection(Double fovy, Double aspect, Double near, Double far) {
		mat4 = MathUtil.Mat.getPerspectiveProjection(fovy, aspect, near, far);
	}
	public Double[][] mat4() {
		return mat4;
	}

}
