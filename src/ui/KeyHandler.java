package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import libs.MathUtil;
import libs.Struct.Obj;
import main.Panel;

public class KeyHandler implements KeyListener {

	public boolean w, a, s, d, shift, crtl, space, up, down, left, right, tab, q, e, i, j, k, l;
	private Panel p;
	private final Double baseMoveSpeed = 0.1;
	private Double moveSpeed = baseMoveSpeed;
	private Double sprintMoveSpeed = moveSpeed * 1.5;
	private Double turnSpeed = 0.025;

	public KeyHandler(Panel p) {
		this.p = p;
	}
	public void update() {

		if (q) {
			p.scaleTex += 1.0;
		}
		if (e) {
			p.scaleTex -= 1.0;
		}

		Double rotation[] = {0.0, 0.0, 0.0};
		moveSpeed = baseMoveSpeed;
		if (shift) {
			moveSpeed = sprintMoveSpeed;
		} else if (crtl) {
			moveSpeed = baseMoveSpeed / 2;
		}

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
		lookVec3(rotation, turnSpeed);

		p.viewObject.rotationVec[0] = Math.clamp(p.viewObject.rotationVec[0], -Math.toRadians(90),
				Math.toRadians(90));
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
		if (tab) {
			moveDir = MathUtil.vec3.sub(moveDir, upDir);
		}
		if (MathUtil.dot(moveDir, moveDir) > 0) {
			moveDir = MathUtil.vec3.normalize(moveDir);
			p.viewObject.translationVec = MathUtil.vec3.add(p.viewObject.translationVec,
					MathUtil.vec3.scale(moveDir, moveSpeed));
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
		moveOBJ(p.objs.get(0));
	}
	public void lookVec3(Double[] vec3, double lookspeed) {
		if (MathUtil.dot(vec3, vec3) > 0) {
			p.viewObject.rotationVec = MathUtil.vec3.add(p.viewObject.rotationVec,
					MathUtil.vec3.scale(MathUtil.vec3.normalize(vec3), lookspeed));
			// System.out.println("turn");
		}
	}

	public void moveOBJ(Obj obj) {

		Double[] moveVec = {0.0, 0.0, 0.0};
		Double[] forawrdDir = MathUtil.vec3.forwardDirTo90(p.viewObject.rotationVec[1]);
		Double[] rightDir = {forawrdDir[2], 0.0, -forawrdDir[0]};

		// System.out.println("[0]" + forawrdDir[0] + ", [2]" + forawrdDir[2]);

		if (i) {
			moveVec = MathUtil.vec3.add(moveVec, forawrdDir);
		}
		if (j) {
			moveVec = MathUtil.vec3.sub(moveVec, rightDir);
		}
		if (k) {
			moveVec = MathUtil.vec3.sub(moveVec, forawrdDir);
		}
		if (l) {
			moveVec = MathUtil.vec3.add(moveVec, rightDir);
		}

		if (MathUtil.dot(moveVec, moveVec) > 0) {
			// System.out.println("here!");
			moveVec = MathUtil.vec3.normalize(moveVec);
			obj.translationVec = MathUtil.vec3.add(obj.translationVec,
					MathUtil.vec3.scale(moveVec, moveSpeed));
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// System.out.println(e.getKeyChar() + " : " + e.getKeyCode());
		boolean toggle = true;
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W :
				this.w = toggle;
				break;
			case KeyEvent.VK_A :
				this.a = toggle;
				break;
			case KeyEvent.VK_S :
				this.s = toggle;
				break;
			case KeyEvent.VK_D :
				this.d = toggle;
				break;
			case KeyEvent.VK_SHIFT :
				this.shift = toggle;
				break;
			case KeyEvent.VK_SPACE :
				this.space = toggle;
				break;
			case KeyEvent.VK_CONTROL :
				this.crtl = toggle;
				break;
			case KeyEvent.VK_UP :
				this.up = toggle;
				break;
			case KeyEvent.VK_LEFT :
				this.left = toggle;
				break;
			case KeyEvent.VK_RIGHT :
				this.right = toggle;
				break;
			case KeyEvent.VK_DOWN :
				this.down = toggle;
				break;
			case KeyEvent.VK_TAB :
				this.tab = toggle;
				break;
			case KeyEvent.VK_Q :
				this.q = toggle;
				break;
			case KeyEvent.VK_E :
				this.e = toggle;
				break;
			case KeyEvent.VK_I :
				this.i = toggle;
				break;
			case KeyEvent.VK_J :
				this.j = toggle;
				break;
			case KeyEvent.VK_K :
				this.k = toggle;
				break;
			case KeyEvent.VK_L :
				this.l = toggle;
				break;

		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		boolean toggle = false;
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W :
				this.w = toggle;
				break;
			case KeyEvent.VK_A :
				this.a = toggle;
				break;
			case KeyEvent.VK_S :
				this.s = toggle;
				break;
			case KeyEvent.VK_D :
				this.d = toggle;
				break;
			case KeyEvent.VK_SHIFT :
				this.shift = toggle;
				break;
			case KeyEvent.VK_SPACE :
				this.space = toggle;
				break;
			case KeyEvent.VK_CONTROL :
				this.crtl = toggle;
				break;
			case KeyEvent.VK_UP :
				this.up = toggle;
				break;
			case KeyEvent.VK_LEFT :
				this.left = toggle;
				break;
			case KeyEvent.VK_RIGHT :
				this.right = toggle;
				break;
			case KeyEvent.VK_DOWN :
				this.down = toggle;
				break;
			case KeyEvent.VK_TAB :
				this.tab = toggle;
				break;
			case KeyEvent.VK_Q :
				this.q = toggle;
				break;
			case KeyEvent.VK_E :
				this.e = toggle;
				break;
			case KeyEvent.VK_I :
				this.i = toggle;
				break;
			case KeyEvent.VK_J :
				this.j = toggle;
				break;
			case KeyEvent.VK_K :
				this.k = toggle;
				break;
			case KeyEvent.VK_L :
				this.l = toggle;
				break;

		}

	}

}
