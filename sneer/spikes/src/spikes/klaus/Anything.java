package spikes.klaus;





public class Anything {

	private static long _lastTime;

	public static void main(String[] args) {
		
		while (true) {
			long now = now();
			if ((now - _lastTime) < (1000L * 1000 * 1000 * 5)) continue;
			System.out.println("" + now + "  " + (now - _lastTime));
			_lastTime = now;
		}

		
//		ImageIO.write(image, "png", output);
		
//		JWindow window = new JWindow();
//		window.setBounds(0, 0, 300, 300);
//		window.setName("Banana");
//		window.setVisible(true);
		
//		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//		DisplayMode mode = device.getDisplayMode();
//		System.out.println("" + mode.getWidth() + " x " + mode.getHeight());
	}
	
	static private long now() {
		return System.nanoTime();
	}


}
