package load;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import libs.Struct.Face;
import libs.Struct.Point3D;

public class OBJLoad {
	private Face[] faces;
	private Point3D[] points;
	private File file;
	private final Color[] bigColor = {Color.red, Color.black, Color.pink, Color.blue, Color.green,
			Color.yellow, Color.orange, Color.MAGENTA, Color.white};
	private int currentColor = 0;

	public OBJLoad() {

	}
	public OBJLoad(File file) {
		this.file = file;
	}
	public void setFile(File file) {
		this.file = file;
	}

	public void load() {
		try {
			BufferedReader read = new BufferedReader(new FileReader(file));

			String line;
			String target;
			String body;
			ArrayList<Point3D> point = new ArrayList<>();
			ArrayList<Face> face = new ArrayList<>();
			while ((line = read.readLine()) != null) {
				System.out.println("current line reading: " + line);

				target = Database.getTarget(line);
				body = line.replace(target, "").trim();
				System.out.println("target: " + target);

				switch (target) {
					case "v" :
						System.out.println("body:" + body);
						point.add(getVertex(body));
						System.out.println("vertex case");
						break;
					case "f" :
						System.out.println("face case");
						String[] b = body.split(" ");
						int spaces = b.length;
						int[] indicies = new int[spaces];

						for (int i = 0; i < spaces; i++) {
							// System.out.println("b:" + b[i].split("/")[0]);
							indicies[i] = Integer.parseInt(b[i].split("/")[0]) - 1;
						}

						face.add(new Face(indicies, bigColor[currentColor]));
						currentColor++;
						break;
				}
			}
			Point3D[] pointOut = new Point3D[point.size()];
			Face[] faceOut = new Face[face.size()];

			for (int i = 0; i < point.size(); i++) {
				pointOut[i] = point.get(i);
			}
			for (int i = 0; i < face.size(); i++) {
				faceOut[i] = face.get(i);
			}
			System.out.println("face out: " + faceOut.length);

			faces = faceOut;
			points = pointOut;
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public Point3D[] getPoints() {
		assert points != null : "broh! you have to load the obj points before using it!";
		return points;
	}
	public Face[] getFaces() {
		assert faces != null : "my guy! you have to load the obj faces before useing it!";
		System.out.println("faces: " + faces.length);
		return faces;

	}
	public static int spaces(String in) {
		return in.length() - in.replaceAll(" ", "").length();
	}
	public static Point3D getVertex(String in) {
		String x;
		String y;
		String z;
		x = Database.getTarget(in);
		in = in.replaceFirst(x, "").trim();
		y = Database.getTarget(in);
		in = in.replaceFirst(y, "").trim();
		z = Database.getTarget(in);
		in = in.replaceFirst(z, "").trim();

		System.out.println("x: " + x);
		System.out.println("y: " + y);
		System.out.println("y: " + z);

		if (!in.trim().equals("")) {
			System.out.println("Huston, we have a problem! " + Thread.currentThread());
		}
		return new Point3D(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));

	}

}
