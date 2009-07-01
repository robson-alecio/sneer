package sneer.foundation.brickness.testsupport;

import static sneer.foundation.environments.Environments.my;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.TestClass;
import org.junit.internal.runners.TestMethod;
import org.junit.runner.notification.RunNotifier;

import sneer.foundation.brickness.Brickness;
import sneer.foundation.environments.Bindings;
import sneer.foundation.environments.CachingEnvironment;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.EnvironmentUtils;
import sneer.foundation.environments.Environments;

public class BrickTestRunner extends IntermittentTestRunner {

	protected static class TestMethodWithEnvironment extends TestMethod {

		private final Environment _environment;
		private final Method _method;

		public TestMethodWithEnvironment(Method test, TestClass testClass) {
			super(test, testClass);
			_environment = my(Environment.class);
			_method = test;
		}

		static class InvocationTargetExceptionEnvelope extends RuntimeException {
			public InvocationTargetExceptionEnvelope(InvocationTargetException e) {
				super(e);
			}
		}

		@Override
		public void invoke(final Object test) throws InvocationTargetException {
			try {
				invokeInEnvironment(test);
			} catch (InvocationTargetExceptionEnvelope e) {
				throw (InvocationTargetException)e.getCause();
			}
		}

		private void invokeInEnvironment(final Object test) {
			tryToRunTestMethodWith(_environment, new Runnable() { @Override public void run() {
				try {
					doInvoke(test);
				} catch (InvocationTargetException e) {
					throw new InvocationTargetExceptionEnvelope(e);
				}
			}}, maxAttemptsFor(_method), _method.getName());
		}

		protected void doInvoke(Object test) throws InvocationTargetException {
			try {
				superInvoke(test);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		private void superInvoke(Object test) throws IllegalAccessException, InvocationTargetException {
			super.invoke(test);
		}
	}

	class TestInstanceEnvironment implements Environment {

		private Object _testInstance;

		@Override
		public <T> T provide(Class<T> intrface) {
			if (intrface.isAssignableFrom(BrickTestRunner.class))
				return (T)BrickTestRunner.this;
			if (intrface.isAssignableFrom(TestInstanceEnvironment.class))
				return (T)this;
			return provideContribution(intrface);
		}

		private <T> T provideContribution(Class<T> intrface) {
			if (_testInstance == null) throw new IllegalStateException();

			for (Field field : _contributedFields) {
				final Object value = fieldValueFor(field, _testInstance);
				if (null == value) {
					assertFieldCantProvide(field, intrface);
					continue;
				}
				if (intrface.isInstance(value))
					return (T)value;
			}
			return null;
		}

		private <T> void assertFieldCantProvide(Field field, Class<T> intrface) {
			if (intrface.isAssignableFrom(field.getType())) {
				throw new IllegalStateException(field + " has not been initialized. You might have to move its declaration up, before it is used indirectly by other declarations.");
			}
		}

		public void instanceBeingInitialized(Object testInstance) {
			if (_testInstance != null) throw new IllegalStateException();
			_testInstance = testInstance;
		}
	}

	private final Field[] _contributedFields; 

	public BrickTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
		_contributedFields = contributedFields(testClass);
	}

	
	private static Field[] contributedFields(Class<? extends Object> klass) {
		final ArrayList<Field> result = new ArrayList<Field>();
		while (klass != Object.class) {
			collectContributedFields(result, klass);
			klass = klass.getSuperclass();
		}
		return result.toArray(new Field[result.size()]);
	}

	private static void collectContributedFields(
			final ArrayList<Field> collector,
			final Class<? extends Object> klass) {

		for (Field field : klass.getDeclaredFields()) {
			if (field.getAnnotation(Bind.class) == null)
				continue;
			collector.add(field);
		}
	}

	protected static Object fieldValueFor(Field field, Object instance) {
		try {
			field.setAccessible(true);
			return field.get(instance);
		} catch (Exception e) {
			throw new IllegalStateException(e); 
		}
	}

	public void instanceBeingInitialized(Object testInstance) {
		my(TestInstanceEnvironment.class).instanceBeingInitialized(testInstance);
	}

	Environment newTestEnvironment(Object... bindings) {
		return new CachingEnvironment(
				EnvironmentUtils.compose(
					new Bindings(bindings).environment(),
					my(TestInstanceEnvironment.class),
					Brickness.newBrickContainer()));
	}

	private Environment newEnvironment() {
		return new CachingEnvironment(
				EnvironmentUtils.compose(
					new TestInstanceEnvironment(),
					Brickness.newBrickContainer()));
	}

	@Override
	protected void invokeTestMethod(final Method testMethod, final RunNotifier notifier) {
		tryToRunTestMethodWith(newEnvironment(), new Runnable() { @Override public void run() {
			superInvokeTestMethod(testMethod, notifier);
		}}, maxAttemptsFor(testMethod), testMethod.getName());
	}

	private static int maxAttemptsFor(final Method testMethod) {
		final Intermittent annotation = testMethod.getAnnotation(Intermittent.class);
		return (annotation == null ? 1 : annotation.maxAttempts());
	}

	// TODO: Removed this method
	private static void tryToRunTestMethodWith(final Environment environment, 
			final Runnable testMethodRunnable, final int maxNumberOfExecutions, final String methodName) {

		if (maxNumberOfExecutions == 1) {
			runTestMethodWith(environment, testMethodRunnable);
		} else {
			tryToRunIntermittentTestMethodWith(environment, testMethodRunnable, maxNumberOfExecutions, methodName);
		}
	}

	private static void tryToRunIntermittentTestMethodWith(final Environment environment,
			final Runnable testMethodRunnable, final int maxNumberOfExecutions, final String methodName) {

		Exception originalException = null;

		for (int attempt = 1; attempt <= maxNumberOfExecutions; ++attempt) {
			try {
				runTestMethodWith(environment, testMethodRunnable);
				assessIntermittence(methodName, attempt, maxNumberOfExecutions);
				break; // If no exception is thrown, leave loop immediately

			} catch (Exception e) {
				if (originalException == null) originalException = e;
				System.err.println("Execution " + attempt + " of " + methodName + " failed. Error: " + e.getMessage());
				if (attempt == maxNumberOfExecutions) throw new RuntimeException(e);
			}
		}
	}

	// TODO: Remove this method and use in-line instead (caller above).
	private static void runTestMethodWith(final Environment environment,
			final Runnable testMethodRunnable) {

		Environments.runWith(environment, testMethodRunnable);
	}

	private static void assessIntermittence(final String methodName,
			final int actualNumberOfExecutions, final int maxNumberOfExecutions) {

		if (maxNumberOfExecutions > 1 && actualNumberOfExecutions == 1)
			System.out.println(">>> " + methodName + " may not need to use @Intermittent anymore");
	}

	@Override
	protected TestMethod wrapMethod(Method method) {
		return new TestMethodWithEnvironment(method, getTestClass());
	}

	protected void superInvokeTestMethod(Method method, RunNotifier notifier) {
		super.invokeTestMethod(method, notifier);
	}

	void dispose() {
		((CachingEnvironment)my(Environment.class)).clear();
	}
}
