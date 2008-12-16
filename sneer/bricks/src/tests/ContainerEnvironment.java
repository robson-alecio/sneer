package tests;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.internal.runners.InitializationError;

import sneer.kernel.container.Containers;
import wheel.lang.Environment;
import wheel.lang.exceptions.WheelExceptionHandler;
import wheel.lang.exceptions.impl.ExceptionLeaker;
import wheel.testutil.WheelEnvironment;
import static wheel.lang.Environments.my;

public class ContainerEnvironment extends WheelEnvironment {
	
	private final Field[] _contributedFields; 
	
	public ContainerEnvironment(Class<?> testClass) throws InitializationError {
		super(testClass);
		_contributedFields = contributedFields(testClass);
	}
	
	class TestEnvironment implements Environment {

		private Object _testInstance;

		@Override
		public <T> T provide(Class<T> intrface) {
			if (intrface == ContainerEnvironment.class)
				return (T)ContainerEnvironment.this;
			if (intrface == TestEnvironment.class)
				return (T)this;
			if (intrface == WheelExceptionHandler.class)
				return (T)new ExceptionLeaker();
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
			if (intrface.isAssignableFrom(field.getClass())) {
				throw new IllegalStateException(field + " has not been initialized.");
			}
		}
		
		public void instanceBeingInitialized(Object testInstance) {
			if (_testInstance != null) throw new IllegalStateException();
			_testInstance = testInstance;
		}
	}
	
	@Override
	protected Environment environment() {
		return Containers.newContainer(new TestEnvironment());
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
		my(TestEnvironment.class).instanceBeingInitialized(testInstance);
	}

}
