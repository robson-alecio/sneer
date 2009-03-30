package sneer.brickness.environment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import sneer.commons.environments.Environment;


public class Brickness implements Environment {

	@Override
	public <T> T provide(Class<T> intrface) {
		if (!intrface.isInterface()) throw new IllegalArgumentException();

		try {
			return instantiate(impl(intrface));
		} catch (ClassNotFoundException e) {
			return null;
		} catch (Exception e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	private <T> Class<T> impl(Class<T> intrface) throws ClassNotFoundException {
		return (Class<T>)Class.forName(implNameFor(intrface)); 
	}

	private <T> T instantiate(Class<T> impl) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Constructor<T> ctor = impl.getDeclaredConstructor();
		ctor.setAccessible(true);
		return ctor.newInstance(); 
	}

	private <T> String implNameFor(Class<T> intrface) {
		return BrickConventions.implClassNameFor(intrface.getName());
	}
}
