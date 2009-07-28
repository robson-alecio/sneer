package spikes.klaus;

import java.lang.ref.WeakReference;


@SuppressWarnings("deprecation")
public class Anything {

	public static void main(String[] args) {

		new Daemon("Spike") { @Override public void run() {
			while (true) {
				new Object();
				//System.out.println("creating");
			}
		}};
		
		WeakReference<Object> weak = new WeakReference<Object>(new Object());
		//Object obj = weak.get();
		while (true) {
//			byte[] lixo = new byte[1000000];
			Thread.yield();
			Object object = weak.get();
			if (object == null) return;
			System.out.println(System.currentTimeMillis());
			object.toString();
			object = null;
			//System.gc();
		}

//		System.out.println(System.nanoTime() % 3);
		
//		for (int i = 0; i <= 0; i++)
//			new Thread("Test " + i) { @Override public void run() {
//				Thread.currentThread().setPriority(MAX_PRIORITY);
//				testTime();
//			}}.start();

		
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

//	private static void testTime() {
//		long t0 = now();
//		int counter = 0;
//
//		while (true) {
//			Threads.sleepWithoutInterruptions(1);
//			long now = now();
//			
//			if (now - t0 > 20L * 1000 * 1000 * 1000) break;
//			counter++;
//		}
//		
//		System.out.println(counter / 20);
//	}
//	
//	static private long now() {
//		return System.nanoTime();
//	}
}
