package sneer.brickness.environment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import sneer.commons.environments.Environment;


public class Brickness implements Environment {

	private final Map<Class<?>, ClassLoader> _classLoadersByBrick;

	public Brickness(Map<Class<?>, ClassLoader> classLoadersByBrick) {
		_classLoadersByBrick = classLoadersByBrick;
	}

	public Brickness() {
		_classLoadersByBrick = null;
	}

	@Override
	public <T> T provide(Class<T> intrface) {
		if (!intrface.isInterface()) throw new IllegalArgumentException();

		try {
			return instantiate(impl(intrface));
		} catch (ClassNotFoundException e) {
			return null;
		} catch (Exception e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet("Exception trying to provide " + intrface, e); // Fix Handle this exception.
		}
	}

	private <T> Class<T> impl(Class<T> intrface) throws ClassNotFoundException {
		ClassLoader classLoader = _classLoadersByBrick == null
			? ClassLoader.getSystemClassLoader()
			: _classLoadersByBrick.get(intrface);
		
		if (classLoader == null) { //Old Logic. Delete this.
			classLoader = ClassLoader.getSystemClassLoader();
//			_allInterfaces = intrface.getSimpleName() + ".class,\n" + _allInterfaces;
//			System.out.println(_allInterfaces);
		}
			
		return (Class<T>) classLoader.loadClass(implNameFor(intrface));
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
