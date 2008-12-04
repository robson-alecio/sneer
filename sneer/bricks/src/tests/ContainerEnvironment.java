package tests;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.internal.runners.InitializationError;

import sneer.kernel.container.Brick;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import wheel.lang.Environment;
import wheel.testutil.WheelEnvironment;

public class ContainerEnvironment extends WheelEnvironment {

	private Container _container; 
	
	public ContainerEnvironment(Class<?> testClass) throws InitializationError {
		super(testClass);
	}
	
	@Override
	protected Environment testMethodEnvironment() {
		return _container;
	}
	
	@Override
	protected Object createTest() throws Exception {
		final Object test = super.createTest();
		_container = ContainerUtils.newContainer(fieldBindings(test));
		return test;
	}

	private Object[] fieldBindings(Object instance) {
		final ArrayList<Object> result = new ArrayList<Object>();
		for (Field field : instance.getClass().getDeclaredFields()) {
			final Object fieldValue = getFieldValue(field, instance);
			if (fieldValue instanceof Brick)
				result.add(fieldValue);
		}
		return result.toArray();
	}

	private Object getFieldValue(Field field, Object instance) {
		try {
			return field.get(instance);
		} catch (Exception e) {
			throw new IllegalStateException(e); 
		}
	}

}
