package sneer.pulp.threadpool.impl;

import static wheel.lang.Environments.my;
import sneer.pulp.exceptionhandling.ExceptionHandler;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;
import wheel.lang.Environments;
import wheel.lang.Threads;
import wheel.lang.Environments.Memento;

class ThreadPoolImpl implements ThreadPool {

	private OwnNameKeeper _ownNameKeeper = my(OwnNameKeeper.class);
	
	
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

	@Override
	public void dispatch(final Memento environment, final Runnable runnable) {
		my(ExceptionHandler.class).shield(new Runnable(){@Override public void run() {
			Environments.runWith(environment, runnable);
		}});
	}

}
