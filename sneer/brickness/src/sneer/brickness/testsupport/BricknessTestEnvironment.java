package sneer.brickness.testsupport;

import static sneer.brickness.Environments.my;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.internal.runners.InitializationError;

import sneer.brickness.CachingEnvironment;
import sneer.brickness.ClosedEnvironment;
import sneer.brickness.ConventionBasedEnvironment;
import sneer.brickness.Environment;
import sneer.brickness.Environments;
import sneer.pulp.exceptionhandling.ExceptionHandler;
import sneer.pulp.exceptionhandling.tests.mocks.ExceptionLeaker;

public class BricknessTestEnvironment extends WheelEnvironment {
	
	private final Field[] _contributedFields; 
	
	public BricknessTestEnvironment(Class<?> testClass) throws InitializationError {
		super(testClass);
		_contributedFields = contributedFields(testClass);
	}
	
	class TestInstanceEnvironment implements Environment {

		private Object _testInstance;

		@Override
		public <T> T provide(Class<T> intrface) {
			if (intrface.isAssignableFrom(BricknessTestEnvironment.class))
				return (T)BricknessTestEnvironment.this;
			if (intrface.isAssignableFrom(TestInstanceEnvironment.class))
				return (T)this;
			if (intrface.isAssignableFrom(ExceptionHandler.class))
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
			if (intrface.isAssignableFrom(field.getType())) {
				throw new IllegalStateException(field + " has not been initialized. You might have to move its declaration up, before it is used indirectly by other declarations.");
			}
		}
		
		public void instanceBeingInitialized(Object testInstance) {
			if (_testInstance != null) throw new IllegalStateException();
			_testInstance = testInstance;
		}
	}
	
	@Override
	protected Environment environment() {
		return newEnvironment();
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

	public Environment newEnvironmentWith(Object... bindings) {
		return new CachingEnvironment(
				Environments.compose(
					new ClosedEnvironment(bindings),
					new TestInstanceEnvironment(),
					new ConventionBasedEnvironment()));
	}

	public Environment newEnvironment() {
		return new CachingEnvironment(
				Environments.compose(
					new TestInstanceEnvironment(),
					new ConventionBasedEnvironment()));
	}
	
}
