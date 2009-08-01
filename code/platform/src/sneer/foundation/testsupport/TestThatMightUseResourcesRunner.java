package sneer.foundation.testsupport;

import java.lang.reflect.Method;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.TestMethod;

public class TestThatMightUseResourcesRunner extends JUnit4ClassRunner {

	public TestThatMightUseResourcesRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	@Override
	protected TestMethod wrapMethod(Method method) {
		return new TestMethodThatMightUseResources(method, getTestClass());
	}

	
	
}