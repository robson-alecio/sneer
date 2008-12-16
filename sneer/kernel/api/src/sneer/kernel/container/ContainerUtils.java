package sneer.kernel.container;

import java.lang.reflect.Constructor;

public class ContainerUtils {
	
	public static Container newContainer(Object... implementationBindings) {
		try {
			final Constructor<?> defaultCtor = Class.forName("sneer.kernel.container.impl.ContainerImpl").getConstructors()[0];
			return (Container) defaultCtor.newInstance(new Object[] { implementationBindings });
		} catch (Exception e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
    }
	
}
