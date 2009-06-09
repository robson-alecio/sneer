package sneer.foundation.brickness.testsupport;

import static sneer.foundation.commons.environments.Environments.my;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.TestClass;
import org.junit.internal.runners.TestMethod;
import org.junit.runner.notification.RunNotifier;

import sneer.foundation.brickness.Brickness;
import sneer.foundation.commons.environments.Bindings;
import sneer.foundation.commons.environments.CachingEnvironment;
import sneer.foundation.commons.environments.Environment;
import sneer.foundation.commons.environments.EnvironmentUtils;
import sneer.foundation.commons.environments.Environments;

public class BrickTestRunner extends JUnit4ClassRunner {

	protected static class TestMethodWithEnvironment extends TestMethod {
		
		private final Environment _environment;

		public TestMethodWithEnvironment(Method method, TestClass testClass) {
			super(method, testClass);
			_environment = my(Environment.class);
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
			if (_testInstance == null)
				return null;
				
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
			if (field.getAnnotation(Contribute.class) == null)
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

	public Environment newTestEnvironment(Object... bindings) {
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
	protected void invokeTestMethod(final Method arg0, final RunNotifier arg1) {
		Environments.runWith(newEnvironment(), new Runnable() { @Override public void run() {
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


	void dispose() {
		((CachingEnvironment)my(Environment.class)).clear();
	}
	
}
