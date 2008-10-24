package spikes.klaus;

import wheel.lang.Threads;





public class Anything {

	public static void main(String[] args) {

		for (int i = 0; i <= 100; i++)
			new Thread("Test " + i) { @Override public void run() {
				testTime();
			}}.start();

		
//		while (true) {
//			long now = now();
//			if ((now - _lastTime) < (1000L * 1000 * 1000 * 5)) continue;
//			System.out.println("" + now + "  " + (now - _lastTime));
//			_lastTime = now;
//		}

		
//		ImageIO.write(image, "png", output);
		
//		JWindow window = new JWindow();
//		window.setBounds(0, 0, 300, 300);
//		window.setName("Banana");
//		window.setVisible(true);
		
//		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//		DisplayMode mode = device.getDisplayMode();
//		System.out.println("" + mode.getWidth() + " x " + mode.getHeight());
	}

	private static void testTime() {
		long t0 = now();
		int counter = 0;

		while (true) {
			Threads.sleepWithoutInterruptions(1);
			long now = now();
			
			if (now - t0 > 10L * 1000 * 1000 * 1000) break;
			counter++;
		}
		
		System.out.println(counter / 10);
	}
	
	static private long now() {
		return System.nanoTime();
	}


}
