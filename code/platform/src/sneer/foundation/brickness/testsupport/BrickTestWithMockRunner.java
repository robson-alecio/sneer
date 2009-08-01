package sneer.foundation.brickness.testsupport;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jmock.Mockery;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.TestMethod;

public class BrickTestWithMockRunner extends BrickTestRunner {

	public BrickTestWithMockRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}
	
	@Override
	protected TestMethod wrapMethod(Method method) {
		return new TestMethodWithEnvironment(method, getTestClass()) {
			
			@Override
			public void invoke(Object test) throws InvocationTargetException {
				super.invoke(test);
				assertMockeryHasBeenSatisfied(test);
			}

			private void assertMockeryHasBeenSatisfied(Object test) {
				final Mockery mockery = mockeryFor(test);
				mockery.assertIsSatisfied();
			}
			
			private Mockery mockeryFor(Object test) {
				Class<?> klass = test.getClass();
				while (klass != Object.class) {
					for (Field field : klass.getDeclaredFields()) {
						if (Mockery.class.isAssignableFrom(field.getType()))
							return (Mockery) fieldValueFor(field, test);
					}
					klass = klass.getSuperclass();
				}
				return null;
			}
		};
	}

}