package sneer.bricks.threadpool.impl;

import sneer.bricks.threadpool.ThreadPool;
import sneer.lego.Inject;
import spikes.legobricks.name.OwnNameKeeper;

public class ThreadPoolImpl implements ThreadPool {

	@Inject
	private OwnNameKeeper _ownNameKeeper;
	
	@Override
	public void runDaemon(Runnable runnable) {
		Thread daemon = new Thread(runnable, inferThreadName());
        daemon.setDaemon(true);
        daemon.start();
	}

	private String inferThreadName() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement element = stackTrace[3];
		String className = toSimpleClassName(element.getClassName());
		String ownName = _ownNameKeeper.name().currentValue();
		
		return ownName + " - " + className + "." + element.getMethodName(); 
	}

	private String toSimpleClassName(String className) {
		return className.substring(className.lastIndexOf(".") + 1);
	}

}
