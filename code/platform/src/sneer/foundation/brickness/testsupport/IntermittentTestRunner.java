package sneer.foundation.brickness.testsupport;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class IntermittentTestRunner extends JUnit4ClassRunner {

	public IntermittentTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	@Override
	protected void invokeTestMethod(final Method method, final RunNotifier notifier) {
		final AtomicInteger attempt = new AtomicInteger(1);

		notifier.addListener(new RunListener() { @Override public void testFailure(Failure failure) throws Exception {
				System.err.println("Execution " + attempt + " of " + method.getName() + " failed.");
				System.err.println(failure.getTrace());
				doInvokeTestMethod(method, notifier, attempt.incrementAndGet());
		}});

		doInvokeTestMethod(method, notifier, attempt.get());
		assessIntermittence(method.getName(), attempt.get(), maxAttemptsFor(method));
	}

	private void doInvokeTestMethod(Method method, RunNotifier notifier, int attempt) {
		if (attempt <= maxAttemptsFor(method)) {
			super.invokeTestMethod(method, notifier);
		}
	}

	private static int maxAttemptsFor(final Method method) {
		final Intermittent annotation = method.getAnnotation(Intermittent.class);
		return (annotation == null ? 1 : annotation.maxAttempts());
	}

	private static void assessIntermittence(final String methodName, final int numberOfAttempts, final int maxAttempts) {
		if (maxAttempts > 1 && numberOfAttempts == 1)
			System.out.println(">>> " + methodName + " may not need to use @Intermittent anymore");
	}
}
