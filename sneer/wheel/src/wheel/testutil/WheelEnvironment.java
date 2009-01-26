package wheel.testutil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.TestClass;
import org.junit.internal.runners.TestMethod;
import org.junit.runner.notification.RunNotifier;

import wheel.io.logging.WheelLogger;
import wheel.io.logging.impl.WheelLoggerImpl;
import wheel.lang.Environment;
import wheel.lang.Environments;
import wheel.lang.Environments.Memento;
import wheel.lang.exceptions.WheelExceptionHandler;
import wheel.lang.exceptions.impl.WheelExceptionLeaker;

public class WheelEnvironment extends JUnit4ClassRunner {
	
	public static class TestMethodWithEnvironment extends TestMethod {
		
		private final Memento _environment;

		public TestMethodWithEnvironment(Method method, TestClass testClass) {
			super(method, testClass);
			_environment = Environments.memento();
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
			Environments.runWith(_environment, new Runnable() { @Override public void run() {
				try {
					doInvoke(test);
				} catch (InvocationTargetException e) {
					throw new InvocationTargetExceptionEnvelope(e);
				}
			}});
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
					return (T) new WheelExceptionLeaker();
				if (WheelLogger.class == intrface)
					return (T) new WheelLoggerImpl();				
				return null;
			}
		};
	}
}