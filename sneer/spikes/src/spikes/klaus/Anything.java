package spikes.klaus;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class Anything {

	public static void main(String[] args) {
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		DisplayMode mode = device.getDisplayMode();
		System.out.println("" + mode.getWidth() + " x " + mode.getHeight());
	}

}
