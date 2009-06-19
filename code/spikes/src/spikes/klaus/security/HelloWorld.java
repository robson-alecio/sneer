package spikes.klaus.security;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import sneer.bricks.hardware.cpu.threads.Threads;

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
		System.err.println("Hello World");
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		my(Threads.class).startDaemon("classloading spike", this);

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
