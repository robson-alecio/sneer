package sneer.bricks.hardware.cpu.threads.impl;

import static sneer.foundation.environments.Environments.my;

import java.lang.ref.WeakReference;

import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Stepper;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.log.Logger;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;
import sneer.foundation.testsupport.Daemon;

@SuppressWarnings("deprecation")
class ThreadsImpl implements Threads {

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
	public void joinWithoutInterruptions(Thread thread) {
		try {
			thread.join();

		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void startDaemon(final String threadName, final Runnable runnable) {
		final Environment environment = my(Environment.class);
		final Latch hasStarted = newLatch();

		new Daemon(threadName) { @Override public void run() {
			hasStarted.open();
			Environments.runWith(environment, runnable);
		}};
		
		hasStarted.waitTillOpen();
	}

	@Override
	public Latch newLatch() {
		return new LatchImpl();
	}

	@Override
	public Stepper newStepper(Steppable stepper) {
		final WeakReference<Steppable> stepperWeakRef = new WeakReference<Steppable>(stepper);
		final String stepperToString = stepper.toString(); 
		startDaemon(inferThreadName(), new Runnable() { @Override public void run() {
			Steppable s;
			do {
				s = stepperWeakRef.get();
				if (s == null) my(Logger.class).log("Stepper {} garbage collected.", stepperToString);
			} while (s != null && s.step());
		}});
		return null;
	}

	private String inferThreadName() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement element = stackTrace[3];
		String className = toSimpleClassName(element.getClassName());
		
		return className + "." + element.getMethodName() + "(" + Threads.class.getClassLoader() + ")"; 
	}

	private static String toSimpleClassName(String className) {
		return className.substring(className.lastIndexOf(".") + 1);
	}

	/**Waits until crashAllThreads() is called. */
	@Override
	public void waitUntilCrash() {
		_crash.waitTillOpen();
	}

	@Override
	public void crashAllThreads() {
		//doCrashAllThreads();
		
		_crash.open();
	}

//	private void doCrashAllThreads() {
//		throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(); // Implement
//	}
}
