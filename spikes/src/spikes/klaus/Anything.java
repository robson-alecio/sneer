package spikes.klaus;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.pulp.threads.Threads;
import sneer.foundation.threads.Daemon;

public class Anything {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		final Threads threads = my(Threads.class);

		Daemon daemon = new Daemon("Spike") {
			@Override
			public void run() {
				System.out.println("Sleeping...");
				try {
					threads.sleepWithoutInterruptions(3000);
				} catch (Error e) {
					System.out.println(e.getClass());
				}
				System.out.println("Sleeping again...");
				threads.sleepWithoutInterruptions(5000);
			}
		};

		threads.sleepWithoutInterruptions(500);
		daemon.stop();
		threads.sleepWithoutInterruptions(5000);
		
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
