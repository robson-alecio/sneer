package sneer.pulp.threads.impl;

import static sneer.commons.environments.Environments.my;

import java.util.HashSet;
import java.util.Set;

import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.threads.Daemon;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.threads.Stepper;
import sneer.pulp.threads.Threads;

class ThreadsImpl implements Threads {

	private static final Set<Object> _reactors = new HashSet<Object>();
	private final OwnNameKeeper _ownNameKeeper = my(OwnNameKeeper.class);

	@Override
	public void waitWithoutInterruptions(Object object) {
		try {
			object.wait();

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sleepWithoutInterruptions(long milliseconds) {
		try {
			Thread.sleep(milliseconds);

		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	/** @param reactor An object that listens to others (is weak referenced by them), such as a Signal Receiver, and has to react. It does not need a actual thread of its own but it cannot be garbage collected.*/
    public void preventFromBeingGarbageCollected(Object reactor) {
		_reactors.add(reactor);
	}

	@Override
	public void joinWithoutInterruptions(Thread thread) {
		try {
			thread.join();

		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public ClassLoader contextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	@Override
	public void startDaemon(String threadName, final Runnable runnable) {
		final Environment environment = my(Environment.class);

		new Daemon(threadName) { @Override public void run() {
			Environments.runWith(environment, runnable);
		}};
	}

	@Override
	public Runnable createNotifier() {
		return new Runnable() { @Override synchronized public void run() {
			notify();
		}};
	}

	@Override
	public void registerActor(Runnable actor) {
		startDaemon(inferThreadName(), actor);
	}

	@Override
	public void registerStepper(final Stepper stepper) {
		startDaemon(inferThreadName(), new Runnable() { @Override public void run() {
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

	private static String toSimpleClassName(String className) {
		return className.substring(className.lastIndexOf(".") + 1);
	}
}
