package sneer.container.impl;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import sneer.brickness.environment.BrickConventions;
import sneer.brickness.environment.Brickness;
import sneer.commons.environments.CachingEnvironment;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.lang.Producer;
import sneer.container.BrickLoadingException;
import sneer.container.Container;

public class ContainerImpl implements Container {
	
	private final Environment _environment;
	private final ContainerEnvironment _containerEnvironment;
	private final ClassLoader _apiClassLoader = createApiClassLoader();
	private final Map<Class<?>, ClassLoader> _classLoadersByBrick = new HashMap<Class<?>, ClassLoader>();

	public ContainerImpl(Object... bindings) {
		
		_containerEnvironment = new ContainerEnvironment();
		_containerEnvironment.bind(bindings);
		
		 _environment = new CachingEnvironment(
				Environments.compose(
					_containerEnvironment,
					new Brickness(_classLoadersByBrick)));
	}

	@Override
	public void runBrick(final String classDirectory) throws IOException {
		String packageName = packageNameFor(classDirectory);
		ClassLoader classLoader = classLoaderFor(classDirectory, packageName);
			
		Class<?> brick = new BrickInterfaceSearch(classDirectory, packageName, classLoader).result();
		
		_classLoadersByBrick.put(brick, classLoader);
		
		Class<?> brickImpl = loadImpl(brick, classLoader);
		_containerEnvironment.bind(instantiateInEnvironment(brickImpl));
	}

	@Override
	public Environment environment() {
		return _environment;
	}

	private ClassLoader createApiClassLoader() {
		return getClass().getClassLoader();
	}


	private URLClassLoader classLoaderFor(final String classDirectory, String packageName) {
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
