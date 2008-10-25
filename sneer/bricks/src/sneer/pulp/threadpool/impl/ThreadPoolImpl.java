package sneer.pulp.threadpool.impl;

import sneer.kernel.container.Inject;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;
import wheel.lang.Threads;

class ThreadPoolImpl implements ThreadPool {

	@Inject
	static private OwnNameKeeper _ownNameKeeper;
	
	
	@Override
	public void registerActor(Runnable actor) {
		Threads.startDaemon(inferThreadName(), actor);
	}

	@Override
	public void registerStepper(final Stepper stepper) {
		Threads.startDaemon(inferThreadName(), new Runnable() { @Override public void run() {
			while (stepper.step());
		}});
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
