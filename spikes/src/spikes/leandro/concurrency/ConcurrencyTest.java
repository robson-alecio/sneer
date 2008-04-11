package spikes.leandro.concurrency;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import wheel.lang.Threads;

public class ConcurrencyTest {

	@Test
	public void testExecutor() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(20);
		List<Future<String>> futures = new ArrayList<Future<String>>();
		for(int i=0 ; i<10 ; i++) {
			Future<String> future = executor.submit(new Task("task "+i));
			futures.add(future);
		}
		long start = System.currentTimeMillis();
		long max = start + 10*1000;
		
		boolean allDone = false;
		while(!allDone) {
			long time = System.currentTimeMillis();
			allDone = true;
			Iterator<Future<String>> it = futures.iterator();
			while (it.hasNext()) {
				Future<String> future = it.next();
				if(future.isDone()) {
					//if(!future.isCancelled()) System.out.println(future.get());
					//else System.out.println("NA");
					
					it.remove();
				} else {
					if(time > max) future.cancel(true);
					allDone = false;
				}
			}
			//Threads.sleepWithoutInterruptions(50);
		}

		//System.out.println("All Done: "+(System.currentTimeMillis() - start));
	}
}

class Task implements Callable<String> {

	private long _period;
	
	private String _id;
	
	public Task(String id) {
		_period = RandomUtils.nextInt(20000);
		_id = id;
		//System.out.println(_id +":"+_period);
		
	}
	
	@Override
	public String call() throws Exception {
		Threads.sleepWithoutInterruptions(_period);
		return _id;
	}
}
