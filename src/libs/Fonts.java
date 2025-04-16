package libs;

import java.awt.Graphics;

public class Fonts {

	public static int getHeight(Graphics g, String text) {
		return (int) g.getFontMetrics().getStringBounds(text, g).getHeight();
	}
	public static int getWidth(Graphics g, String text) {
		return (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
	}

}
