package sneer.kernel.container;

import java.lang.reflect.Constructor;

import wheel.lang.Environment;

public class Containers {
	
	public static Container newContainer(Object... implementationBindings) {
		return newContainer(null, implementationBindings);
	}
	
	public static Container newContainer(Environment environment, Object... implementationBindings) {
		try {
			final Constructor<?> defaultCtor = Class.forName("sneer.kernel.container.impl.ContainerImpl").getConstructors()[0];
			return (Container) defaultCtor.newInstance(new Object[] { environment, implementationBindings });
		} catch (Exception e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
    }
	
}
