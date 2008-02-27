package spikes.legobricks.threadpool.impl;

import spikes.legobricks.threadpool.ThreadPool;

public class ThreadPoolImpl implements ThreadPool {

	@Override
	public void run(Runnable runnable) {
		new Thread(runnable).start();
	}

}
