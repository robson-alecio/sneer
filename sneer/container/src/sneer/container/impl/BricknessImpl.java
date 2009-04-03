package sneer.container.impl;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import sneer.brickness.impl.BrickConventions;
import sneer.commons.environments.Bindings;
import sneer.commons.environments.CachingEnvironment;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.lang.Producer;
import sneer.container.BrickLoadingException;
import sneer.container.Brickness;

public class BricknessImpl implements Brickness {
	
	private final Environment _environment;
	private final Bindings _bindings;
	private final ClassLoader _apiClassLoader = createApiClassLoader();

	public BricknessImpl(Object... bindings) {
		_bindings = new Bindings();
		_bindings.bind(bindings);
		
		 _environment = new CachingEnvironment(_bindings.environment());
	}


	@Override
	public void runBrick(File classRootDirectory, String brickName) {
		try {
			tryToRunBrick(classRootDirectory, brickName);
		} catch (Exception e) {
			throw new BrickLoadingException(e);
		}
	}


	private void tryToRunBrick(File classRootDirectory, String brickName) throws ClassNotFoundException {
		ClassLoader classLoader = newImplPackageLoader(classRootDirectory, brickName);
		Class<?> brickImpl = classLoader.loadClass(implNameFor(brickName));
		_bindings.bind(instantiateInEnvironment(brickImpl));
	}


	@Override
	public Environment environment() {
		return _environment;
	}

	private ClassLoader createApiClassLoader() {
		return ClassLoader.getSystemClassLoader(); //TODO See roadmap
	}


	private ClassLoader newImplPackageLoader(File classRootDirectory, String brickName) {
		String implPackage = BrickConventions.implPackageFor(brickName);
		return new ClassLoaderForPackage(classRootDirectory, implPackage, _apiClassLoader);
	}

	private Object instantiateInEnvironment(final Class<?> brickImpl) {
		return Environments.produceWith(_environment, new Producer<Object>() { @Override public Object produce() {
			try {
				return newInstance(brickImpl);
			} catch (Exception e) {
				throw new BrickLoadingException(e);
			}
		}});
	}


	private Object newInstance(Class<?> brickImpl) throws NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalArgumentException, IllegalAccessException {
		Constructor<?> constructor = brickImpl.getDeclaredConstructor();
		constructor.setAccessible(true);
		return constructor.newInstance();
	}

	private String implNameFor(final String brickInterfaceName) {
		return BrickConventions.implClassNameFor(brickInterfaceName);
	}

}
