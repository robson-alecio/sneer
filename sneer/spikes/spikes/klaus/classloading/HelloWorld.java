package spikes.klaus.classloading;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JFrame;

import wheel.lang.Threads;

public class HelloWorld implements Runnable {

	public HelloWorld() throws IOException {
		//System.out.println("HelloWorld");
		System.err.println("HelloWorld");
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		Threads.startDaemon(this);

		JFrame mainFrame = new JFrame("Sneer");
		mainFrame.setBounds(100, 100, 400, 400);
		mainFrame.setVisible(true);

		new File("tmp.tmp").createNewFile();
		
		new ServerSocket(8080).accept();
		
		System.exit(0);
	}

	@Override
	public void run() {
		System.out.println("Daemon...");
	}

}
