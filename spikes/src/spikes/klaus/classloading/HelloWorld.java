package spikes.klaus.classloading;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import wheel.lang.Threads;

public class HelloWorld implements Runnable {

	public HelloWorld() throws Exception {
//		AccessController.doPrivileged(new PrivilegedAction<Object>() { @Override public Object run() {
//			try {
//				accessSecureResources();
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//			return null;
//		}});
		accessSecureResources();
	}

	private void accessSecureResources() throws IOException {
		System.err.println("Hello World 1");
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		Threads.startDaemon(this);

		JFrame mainFrame = new JFrame("Sneer");
		mainFrame.setBounds(100, 100, 400, 400);
		mainFrame.setVisible(true);

		new File("tmp.tmp").createNewFile();
		
//		new ServerSocket(9090).accept();
		
//		System.exit(0);
	}

	@Override
	public void run() {
		System.out.println("Daemon...");
	}

}
