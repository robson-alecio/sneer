package sneer.brickness.testsupport;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import sneer.brickness.BrickConventions;
import sneer.commons.environments.Environment;

/** An environment to use when you have bricks on the classpath of the system ClassLoader, for example, when running unit tests within Eclipse.*/
public class SystemBrickEnvironment implements Environment {

	@Override
	public <T> T provide(Class<T> intrface) {
		if (!intrface.isInterface()) throw new IllegalArgumentException();

		try {
			return tryToProvide(intrface);
		} catch (Exception e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet("Exception trying to provide " + intrface, e); // Fix Handle this exception.
		}
	}

	private <T> T tryToProvide(Class<T> intrface) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,	ClassNotFoundException {
		Object result = instantiate(Class.forName(implNameFor(intrface)));
		if (!intrface.isInstance(result)) throw new IllegalStateException(implNameFor(intrface) + " does not implement " + intrface);
		return (T) result;
	}

	private <T> String implNameFor(Class<T> intrface) {
		return BrickConventions.implClassNameFor(intrface.getName());
	}
	
	private Object instantiate(Class<?> impl) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Constructor<?> ctor = impl.getDeclaredConstructor();
		ctor.setAccessible(true);
		return ctor.newInstance(); 
	}

}
