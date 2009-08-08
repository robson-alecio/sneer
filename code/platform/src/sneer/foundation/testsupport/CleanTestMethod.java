package sneer.foundation.testsupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.internal.runners.TestClass;
import org.junit.internal.runners.TestMethod;

public class CleanTestMethod extends TestMethod {

	public CleanTestMethod(Method method, TestClass testClass) {
		super(method, testClass);
	}

	@Override
	public void invoke(Object test) throws InvocationTargetException, IllegalAccessException {
		super.invoke(test);
		
		//Will only happen if the test passes (if InvocationTargetException is not thrown above).
		if (!(test instanceof CleanTest)) return;
		((CleanTest)test).afterSuccessfulTest();
	}

}