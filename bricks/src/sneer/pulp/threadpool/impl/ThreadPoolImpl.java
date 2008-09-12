package sneer.pulp.threadpool.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.kernel.container.Inject;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.threadpool.ThreadPool;
import wheel.lang.Threads;

public class ThreadPoolImpl implements ThreadPool {

	@Inject
	static private OwnNameKeeper _ownNameKeeper;
	
	private final List<Runnable> _actors = new ArrayList<Runnable>();
	
	
	@Override
	public void registerActor(Runnable actor) {
		_actors.add(actor);
		Threads.startDaemon(inferThreadName(), actor);
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
