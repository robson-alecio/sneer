package sneer.foundation.testsupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.internal.runners.TestClass;
import org.junit.internal.runners.TestMethod;

public class TestMethodThatMightUseResources extends TestMethod {

	public TestMethodThatMightUseResources(Method method, TestClass testClass) {
		super(method, testClass);
	}

	@Override
	public void invoke(Object test) throws InvocationTargetException, IllegalAccessException {
		super.invoke(test);
		
		//Will only happen if the test passes (InvocationTargetException is not thrown above).
		if (!(test instanceof TestThatMightUseResources)) return;
		((TestThatMightUseResources)test).afterSuccessfulTest();
	}

}