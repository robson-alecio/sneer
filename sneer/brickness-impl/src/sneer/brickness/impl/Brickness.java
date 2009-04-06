package sneer.brickness.impl;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import sneer.brickness.BrickConventions;
import sneer.commons.environments.Bindings;
import sneer.commons.environments.CachingEnvironment;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.lang.Producer;

public class Brickness {
	
	private final Environment _environment;
	private final Bindings _bindings;
	private final ClassLoader _apiClassLoader = createApiClassLoader();
	private final BrickDecorator _brickDecorator;

	public Brickness(Object... bindings) {
		this(nullDecorator(), bindings);
	}

	public Brickness(BrickDecorator brickDecorator, Object... bindings) {
		_brickDecorator = brickDecorator;
		
		_bindings = new Bindings();
		_bindings.bind(bindings);
		
		 _environment = new CachingEnvironment(_bindings.environment());
	}

	public void placeBrick(File classRootDirectory, String brickName) {
		try {
			tryToPlaceBrick(classRootDirectory, brickName);
		} catch (Exception e) {
			throw new BrickPlacementException(e);
		}
	}

	public Environment environment() {
		return _environment;
	}
	

	private void tryToPlaceBrick(File classRootDirectory, String brickName) throws ClassNotFoundException {
		ClassLoader classLoader = newImplPackageLoader(classRootDirectory, brickName);
		Class<?> brick = _apiClassLoader.loadClass(brickName);
		Class<?> brickImpl = classLoader.loadClass(implNameFor(brickName));
		_bindings.bind(decorate(brick, instantiateInEnvironment(brickImpl)));
	}


	private Object decorate(Class<?> brick, Object brickImpl) {
		Object result = _brickDecorator.decorate(brick, brickImpl);
		if (!brick.isInstance(result)) throw new IllegalStateException();
		return result;
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
				throw new BrickPlacementException(e);
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

	private static BrickDecorator nullDecorator() {
		return new BrickDecorator() { @Override public Object decorate(Class<?> brick, Object brickImpl) {
			return brickImpl;
		}};
	}
}

