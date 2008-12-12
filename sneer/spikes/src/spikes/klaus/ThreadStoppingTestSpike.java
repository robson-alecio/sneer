package spikes.klaus;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

import wheel.io.Logger;
import wheel.lang.Threads;

public class ThreadStoppingTestSpike {

	static private Thread _thread;


	@Test (timeout = 3000)
	public void test() {
		System.out.println(System.getProperty("Lixo"));
		System.out.println(Thread.currentThread());
		System.out.println(_thread == Thread.currentThread());
	}

	
	@Test (timeout = 3000)
	public void ztest2() {
		Logger.redirectTo(System.out);

		System.out.println(Thread.currentThread());
		_thread = Thread.currentThread();
		System.setProperty("Lixo", "banana");
		
		final Lock monitor = new ReentrantLock();
		new Thread("test") { @Override public void run() {
			monitor.lock(); try{
				try {
					System.out.println("Holding...");
					while (true);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			} finally {monitor.unlock();}
		}}.start();
		
		Threads.sleepWithoutInterruptions(1000);

		
		try {
			System.out.println("Trying...");
			monitor.lockInterruptibly(); try {
				System.out.println("Managed");
			} finally {monitor.unlock();}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}


}
