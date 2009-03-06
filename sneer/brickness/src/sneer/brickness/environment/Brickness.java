package sneer.brickness.environment;

import java.lang.reflect.Constructor;

import sneer.commons.environments.Environment;


public class Brickness implements Environment {

	@Override
	public <T> T provide(Class<T> intrface) {
		if (!intrface.isInterface())
			throw new IllegalArgumentException();
		
		Class<T> implClass;
		try {
			implClass = (Class<T>) Class.forName(implementationNameFor(intrface));
		} catch (ClassNotFoundException e) {
			return null;
		}
		try {
			return instantiate(implClass);
		} catch (Exception e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	private <T> T instantiate(Class<T> implClass)
			throws Exception {
		final Constructor<T> ctor = implClass.getDeclaredConstructor();
		ctor.setAccessible(true);
		return ctor.newInstance();
	}

	private <T> String implementationNameFor(Class<T> intrface) {
		return BrickConventions.implementationNameFor(intrface.getName());
	}
}
