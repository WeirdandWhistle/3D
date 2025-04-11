package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Panel;
import mathUtil.MathUtil;

public class KeyHandler implements KeyListener {

	public boolean w, a, s, d, shift, crtl, space, up, down, left, right;
	public Double startSpeed = 0.07;
	public Double speed = startSpeed;
	public Double sprintSpeed = speed * 1.5;
	private Panel p;

	public KeyHandler(Panel p) {
		this.p = p;
	}
	public void update() {
		Double rotation[] = {0.0, 0.0, 0.0};

		if (up) {
			rotation[0] += 1.0;
			// System.out.println("up");
		}
		if (down) {
			rotation[0] -= 1.0;
		}
		if (left) {
			rotation[1] -= 1.0;
		}
		if (right) {
			rotation[1] += 1.0;
		}
		lookVec3(rotation, 0.025);

		p.viewObject.rotationVec[0] = Math.clamp(p.viewObject.rotationVec[0], -1.5, 1.5);
		// p.viewObject.rotationVec[1] = p.viewObject.rotationVec[1] % (Math.PI
		// * 2);

		Double yaw = p.viewObject.rotationVec[1];
		final Double[] forwardDir = {Math.sin(yaw), 0.0, Math.cos(yaw)};
		final Double[] rightDir = {forwardDir[2], 0.0, -forwardDir[0]};
		final Double[] upDir = {0.0, -1.0, 0.0};

		Double[] moveDir = {0.0, 0.0, 0.0};
		// System.out.println("z: " + forwardDir[2]);

		if (w) {

			moveDir = MathUtil.vec3.add(moveDir, forwardDir);
		}
		if (s) {
			moveDir = MathUtil.vec3.sub(moveDir, forwardDir);
		}
		if (d) {
			moveDir = MathUtil.vec3.add(moveDir, rightDir);
		}
		if (a) {
			moveDir = MathUtil.vec3.sub(moveDir, rightDir);
		}
		if (space) {
			moveDir = MathUtil.vec3.add(moveDir, upDir);
		}
		if (crtl) {
			moveDir = MathUtil.vec3.sub(moveDir, upDir);
		}
		if (MathUtil.dot(moveDir, moveDir, 1) > 0) {
			moveDir = MathUtil.vec3.normalize(moveDir);
			p.viewObject.translationVec = MathUtil.vec3.add(p.viewObject.translationVec,
					MathUtil.vec3.scale(moveDir, 0.05));
			// System.out.println("move: " + p.obj.translationVec[0] + ", " +
			// p.obj.translationVec[1]
			// + ", " + p.obj.translationVec[2] + ", ");
			// System.out.println("forwaradDIr: " + forwardDir[0] + ", " +
			// forwardDir[1] + ", "
			// + forwardDir[2] + ", ");
			// System.out.println(
			// "moveDIr: " + moveDir[0] + ", " + moveDir[1] + ", " + moveDir[2]
			// + ", ");
		}
	}
	public void lookVec3(Double[] vec3, double lookspeed) {
		if (MathUtil.dot(vec3, vec3, 1) > 0) {
			p.viewObject.rotationVec = MathUtil.vec3.add(p.viewObject.rotationVec,
					MathUtil.vec3.scale(MathUtil.vec3.normalize(vec3), lookspeed));
			// System.out.println("turn");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyChar() + " : " + e.getKeyCode());
		boolean toggle = true;
		switch (e.getKeyCode()) {
			case 87 :
				w = toggle;
				break;
			case 65 :
				a = toggle;
				break;
			case 83 :
				s = toggle;
				break;
			case 68 :
				d = toggle;
				break;
			case 16 :
				shift = toggle;
				break;
			case 32 :
				space = toggle;
				break;
			case 17 :
				crtl = toggle;
				break;
			case KeyEvent.VK_UP :
				up = toggle;
				break;
			case KeyEvent.VK_LEFT :
				left = toggle;
				break;
			case KeyEvent.VK_RIGHT :
				right = toggle;
				break;
			case KeyEvent.VK_DOWN :
				down = toggle;
				break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		boolean toggle = false;
		switch (e.getKeyCode()) {
			case 87 :
				w = toggle;
				break;
			case 65 :
				a = toggle;
				break;
			case 83 :
				s = toggle;
				break;
			case 68 :
				d = toggle;
				break;
			case 16 :
				shift = toggle;
				break;
			case 32 :
				space = toggle;
				break;
			case 17 :
				crtl = toggle;
				break;
			case KeyEvent.VK_UP :
				up = toggle;
				break;
			case KeyEvent.VK_LEFT :
				left = toggle;
				break;
			case KeyEvent.VK_RIGHT :
				right = toggle;
				break;
			case KeyEvent.VK_DOWN :
				down = toggle;
				break;
		}

	}

}
