package sneer.container.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import sneer.brickness.environment.BrickConventions;
import sneer.commons.environments.Environments;
import sneer.commons.lang.Producer;
import sneer.container.BrickLoadingException;
import sneer.container.NewContainer;

public class NewContainerImpl implements NewContainer {
	
	private final ContainerEnvironment _environment = new ContainerEnvironment();

	@Override
	public void runBrick(final String classDirectory) throws IOException {
		String brickPackage = packageNameFor(classDirectory);
		URLClassLoader classLoader = classLoaderFor(classDirectory, brickPackage);
			
		Class<?> brick = new BrickInterfaceSearch(classDirectory, brickPackage, classLoader).result();
			
		Class<?> brickImpl = loadImpl(brick, classLoader);
		_environment.bind(instantiateInEnvironment(brickImpl));
	}


	private URLClassLoader classLoaderFor(final String classDirectory, String brickPackage) {
//		return new URLClassLoader(new URL[] { toURL(
//				classpathRootFor(classDirectory, brickPackage)) }, _parentClassLoaderWithCommonsAndContainerApiSuchAsBrickAnnotation);
		return new URLClassLoader(new URL[] { toURL(
				classpathRootFor(classDirectory, brickPackage)) });
	}

	private Class<?> loadImpl(Class<?> brick, URLClassLoader classLoader) {
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

	private URL toURL(File file) {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

	private String packageNameFor(String classDirectory) throws IOException {
		return PackageNameRetriever.packageNameFor(classDirectory);
	}

	private File classpathRootFor(String classDirectory, String packageName) {
		return new File(classDirectory.substring(0, classDirectory.length() - packageName.length()));
	}
	
	private String implNameFor(final String brickInterfaceName) {
		return BrickConventions.implementationNameFor(brickInterfaceName);
	}

}
