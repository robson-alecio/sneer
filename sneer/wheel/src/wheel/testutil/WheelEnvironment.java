package wheel.testutil;

import java.lang.reflect.Method;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.notification.RunNotifier;

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
	protected void invokeTestMethod(final Method arg0, final RunNotifier arg1) {
		Environment.runWith(environmentProvider(), new Runnable() { @Override public void run() {
			superInvokeTestMethod(arg0, arg1);
		}});
	}
	
	protected void superInvokeTestMethod(Method arg0, RunNotifier arg1) {
		super.invokeTestMethod(arg0, arg1);
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