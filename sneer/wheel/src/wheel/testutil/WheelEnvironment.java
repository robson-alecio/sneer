package wheel.testutil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.TestClass;
import org.junit.internal.runners.TestMethod;
import org.junit.runner.notification.RunNotifier;

import wheel.lang.Environment;
import wheel.lang.Environments;
import wheel.lang.Environments.Memento;
import wheel.lang.exceptions.WheelExceptionHandler;
import wheel.lang.exceptions.impl.ExceptionLeaker;

public class WheelEnvironment extends JUnit4ClassRunner {
	
	public static class TestMethodWithEnvironment extends TestMethod {
		private final Memento _environment;

		public TestMethodWithEnvironment(Method method, TestClass testClass) {
			super(method, testClass);
			_environment = Environments.memento();
		}

		@Override
		public void invoke(final Object test) {
			Environments.runWith(_environment, new Runnable() { @Override public void run() {
				doInvoke(test);
			}});
		}

		protected void doInvoke(Object test) {
			try {
				superInvoke(test);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				Throwable tx = e.getTargetException();
				if (tx instanceof RuntimeException) throw (RuntimeException)tx;
				if (tx instanceof Error) throw (Error)tx;
				throw new RuntimeException(tx);
			}
		}

		private void superInvoke(Object test) throws IllegalAccessException,
				InvocationTargetException {
			super.invoke(test);
		}
	}

	public WheelEnvironment(Class<?> testClass) throws InitializationError {
		super(testClass);
	}
	
	@Override
	protected void invokeTestMethod(final Method arg0, final RunNotifier arg1) {
		Environments.runWith(environment(), new Runnable() { @Override public void run() {
			superInvokeTestMethod(arg0, arg1);
		}});
	}
	
	@Override
	protected TestMethod wrapMethod(Method method) {
		return new TestMethodWithEnvironment(method, getTestClass());
	}
	
	protected void superInvokeTestMethod(Method arg0, RunNotifier arg1) {
		super.invokeTestMethod(arg0, arg1);
	}
	
	protected Environment environment() {
		return new Environment() {
			@Override
			public <T> T provide(Class<T> intrface) {
				if (WheelExceptionHandler.class == intrface)
					return (T) new ExceptionLeaker();
				return null;
			}
		};
	}
}