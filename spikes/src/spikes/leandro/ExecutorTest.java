package spikes.leandro;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.*;

import wheel.lang.Threads;

public class ExecutorTest {

	@Test
	public void testExecutor() throws Exception {
		Executor executor = Executors.newCachedThreadPool();
		executor.execute(newTask("a"));
		executor.execute(newTask("b"));
		executor.execute(newTask("c"));
		Threads.sleepWithoutInterruptions(3000);
	}

	private Runnable newTask(final String tag) {
		Runnable result = new Runnable() { @Override public void run() {
			for(int i = 0; i < 10 ; i++) {
				System.out.println(Thread.currentThread().getName() + " " + tag +" " + i);
				Threads.sleepWithoutInterruptions(100);
			}
		}};
		return result;
	}
}
