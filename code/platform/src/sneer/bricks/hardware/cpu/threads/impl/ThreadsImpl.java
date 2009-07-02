package sneer.bricks.hardware.cpu.threads.impl;

import static sneer.foundation.environments.Environments.my;

import java.lang.ref.WeakReference;

import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Stepper;
import sneer.bricks.hardware.cpu.threads.Threads;
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
			hasStarted.trip();
			Environments.runWith(environment, runnable);
		}};
		
		hasStarted.await();
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
		
		return className + "." + element.getMethodName() + "(" + Threads.class.getClassLoader() + ")"; 
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
