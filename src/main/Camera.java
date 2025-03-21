package main;
import mathUtil.Struct.Point3D;
public class Camera {

	public Point3D pos, angle;

	public Camera(Point3D pos, Point3D angle) {
		this.pos = pos;
		this.angle = angle;
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

}
