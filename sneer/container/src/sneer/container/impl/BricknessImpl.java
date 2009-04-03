package sneer.container.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import sneer.brickness.environment.BrickConventions;
import sneer.commons.environments.CachingEnvironment;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.lang.Producer;
import sneer.container.BrickLoadingException;
import sneer.container.Brickness;

public class BricknessImpl implements Brickness {
	
	private final Environment _environment;
	private final EnvironmentWithBindings _bindings;
	private final ClassLoader _apiClassLoader = createApiClassLoader();

	public BricknessImpl(Object... bindings) {
		_bindings = new EnvironmentWithBindings();
		_bindings.bind(bindings);
		
		 _environment = new CachingEnvironment(_bindings);
	}


	@Override
	public void runBrick(final File interfaceFile) throws IOException {
		String classDirectory = interfaceFile.getParent();
		
		String packageName = packageNameFor(classDirectory);
		ClassLoader classLoader = newBrickLoaderFor(classDirectory, packageName);
			
		Class<?> brick = new BrickInterfaceSearch(classDirectory, packageName, classLoader).result();
		
		Class<?> brickImpl = loadImpl(brick, classLoader);
		_bindings.bind(instantiateInEnvironment(brickImpl));
	}

	@Override
	public Environment environment() {
		return _environment;
	}

	private ClassLoader createApiClassLoader() {
		return ClassLoader.getSystemClassLoader(); //TODO See roadmap
	}


	private ClassLoader newBrickLoaderFor(final String classDirectory, String packageName) {
		return new BrickClassLoader(classDirectory, packageName, _apiClassLoader);
	}

	private Class<?> loadImpl(Class<?> brick, ClassLoader classLoader) {
		try {
			return classLoader.loadClass(implNameFor(brick.getName()));
		} catch (ClassNotFoundException e) {
			throw new BrickLoadingException(e);
		}
	}
	
	private Object instantiateInEnvironment(final Class<?> brickImpl) {
		return Environments.produceWith(_environment, new Producer<Object>() { @Override public Object produce() {
			try {
				
				return newInstance(brickImpl);
				
			} catch (InstantiationException e) {
				throw new BrickLoadingException(e);
			} catch (NoSuchMethodException e) {
				throw new BrickLoadingException(e);
			} catch (InvocationTargetException e) {
				throw new BrickLoadingException(e.getCause());
			}
		}});
	}


	private Object newInstance(Class<?> brickImpl) throws NoSuchMethodException, InstantiationException, InvocationTargetException {
		Constructor<?> constructor = brickImpl.getDeclaredConstructor();
		constructor.setAccessible(true);
		try {
			return constructor.newInstance();
		} catch (IllegalAccessException e) {
			throw new IllegalStateException();
		}
	}


	private String packageNameFor(String classDirectory) throws IOException {
		return PackageNameRetriever.packageNameFor(classDirectory);
	}
	
	private String implNameFor(final String brickInterfaceName) {
		return BrickConventions.implClassNameFor(brickInterfaceName);
	}

}
