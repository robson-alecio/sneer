package sneer.bricks.pulp.threads.impl;

import static sneer.foundation.commons.environments.Environments.my;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.bricks.pulp.threads.Latch;
import sneer.bricks.pulp.threads.Stepper;
import sneer.bricks.pulp.threads.Threads;
import sneer.foundation.commons.environments.Environment;
import sneer.foundation.commons.environments.Environments;
import sneer.foundation.commons.threads.Daemon;

class ThreadsImpl implements Threads {

	private static final Set<Object> _reactors = new HashSet<Object>();
	private final OwnNameKeeper _ownNameKeeper = my(OwnNameKeeper.class);
	private final Latch _crash = newLatch();

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
	public void startDaemon(String threadName, final Runnable runnable) {
		final Environment environment = my(Environment.class);

		new Daemon(threadName) { @Override public void run() {
			Environments.runWith(environment, runnable);
		}};
	}

	@Override
	public Latch newLatch() {
		return new LatchImpl();
	}

	@Override
	public void registerStepper(Stepper stepper) {
		final WeakReference<Stepper> stepperWeakRef = new WeakReference<Stepper>(stepper);
		startDaemon(inferThreadName(), new Runnable() { @Override public void run() {
			Stepper s;
			do {
				s = stepperWeakRef.get();
			} while(s != null && s.step());
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

	/**Waits until crashAllThreads() is called. */
	@Override
	public void waitUntilCrash() {
		_crash.await();
	}

	@Override
	public void crashAllThreads() {
		//doCrashAllThreads();
		
		_crash.trip();
	}

//	private void doCrashAllThreads() {
//		throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(); // Implement
//	}
}
