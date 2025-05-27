package load;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {

	public File file;
	public ArrayList<String> tables = new ArrayList<>();
	public HashMap<String, Object[]> parms = new HashMap<>();

	public Database(File file) {
		this.file = file;
	}

	public void loadNewType(String line) {
		String target = getTarget(line);
		String body = line.replace(target, "").trim();

		String type = getTarget(target, '-').trim();
		String name = target.replace(type, "").trim();

		int parmNum = body.length() - body.replaceAll(":", "").length();

		switch (type) {
			case "tables" :
				tables.add(name);
				// parms.add("name",);
				break;
		}
	}

	public static String getTarget(String s) {
		assert !s.equals("") || s == null : "s is just gone";
		String out = "";
		int i = 0;
		// System.out.println(s.length());
		while ((s.charAt(i)) != ' ' && i < s.length() - 1) {
			// System.out.println("i:" + i);
			// System.out.println("char:" + s.charAt(i));
			out += s.charAt(i);
			i++;

		}
		return out;
	}
	public static String getTarget(String s, char key) {
		String out = "";
		int i = 0;
		while ((s.indexOf(i)) != key) {
			out += s.indexOf(i);
			i++;
		}
		return out;
	}
}
