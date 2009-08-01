package sneer.foundation.testsupport;

import java.lang.reflect.Method;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.TestMethod;

public class CleanTestRunner extends JUnit4ClassRunner {

	public CleanTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	@Override
	protected TestMethod wrapMethod(Method method) {
		return new CleanTestMethod(method, getTestClass());
	}

	
	
}