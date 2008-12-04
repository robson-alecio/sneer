package tests;

import org.junit.internal.runners.InitializationError;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import wheel.lang.Environment;
import wheel.testutil.JMockWheelEnvironment;

public class JMockContainerEnvironment extends JMockWheelEnvironment {
	private Container _container; 
	
	public JMockContainerEnvironment(Class<?> testClass) throws InitializationError {
		super(testClass);
	}
	
	@Override
	protected Environment testMethodEnvironment() {
		return new Environment() {
			@Override
			public <T> T provide(Class<T> intrface) {
				if (_container == null) return null;
				return _container.provide(intrface);
			}
		};
	}
	
	@Override
	protected Object createTest() throws Exception {
		final Object test = super.createTest();
		_container = ContainerUtils.newContainer(ContainerEnvironment.boundBrickFieldsFor(test));
		return test;
	}
}