package wheel.testutil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.TestMethod;

import wheel.lang.ByRef;
import wheel.lang.Environment;
import wheel.lang.Environment.Provider;
import wheel.lang.exceptions.WheelExceptionHandler;
import wheel.lang.exceptions.impl.WheelExceptionHandlerImpl;

public class WheelEnvironment extends JUnit4ClassRunner {
	
	public WheelEnvironment(Class<?> testClass) throws InitializationError {
		super(testClass);
	}
	
	@Override
	protected Object createTest() throws Exception {
		final ByRef<Object> result = ByRef.newInstance();
		Environment.runWith(environmentProvider(), new Runnable() { @Override public void run()  {
			try {
				result.value = superCreateTest();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}});
		return result.value;
	}
	
	private Object superCreateTest() throws Exception {
		return super.createTest();
	}
	
	@Override
	protected TestMethod wrapMethod(Method method) {
		System.out.println("wrapping...");
		return new TestMethod(method, this.getTestClass()) {
			@Override
			public void invoke(final Object test) {
				Environment.runWith(environmentProvider(), new Runnable() { @Override public void run() {
					try {
						superInvoke(test);
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					} catch (InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				}});
			}

			private void superInvoke(Object test) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
				super.invoke(test);
			}
		};
	}
	
	private Provider environmentProvider() {
		return new Environment.Provider() {
			@Override
			public <T> T provide(Class<T> intrface) {
				if (WheelExceptionHandler.class == intrface)
					return (T) new WheelExceptionHandlerImpl();
				return null;
			}
		};
	}
}