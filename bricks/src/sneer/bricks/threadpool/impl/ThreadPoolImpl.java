package sneer.bricks.threadpool.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.bricks.name.OwnNameKeeper;
import sneer.bricks.threadpool.ThreadPool;
import sneer.lego.Inject;

public class ThreadPoolImpl implements ThreadPool {

	@Inject
	static private OwnNameKeeper _ownNameKeeper;
	
	private final List<Runnable> _actors = new ArrayList<Runnable>();
	
	
	@Override
	public void registerActor(Runnable actor) {
		_actors.add(actor);

		Thread daemon = new Thread(actor, inferThreadName());
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
