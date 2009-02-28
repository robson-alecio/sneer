package sneer.brickness;

import java.lang.reflect.Constructor;

public class ConventionBasedEnvironment implements Environment {

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
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	private <T> T instantiate(Class<T> implClass)
			throws Exception {
		final Constructor<T> ctor = implClass.getDeclaredConstructor();
		ctor.setAccessible(true);
		return ctor.newInstance();
	}

	private <T> String implementationNameFor(Class<T> intrface) {
		return Conventions.implementationNameFor(intrface.getName());
	}
}
