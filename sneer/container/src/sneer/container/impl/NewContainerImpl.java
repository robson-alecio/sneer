package sneer.container.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import sneer.brickness.environment.BrickConventions;
import sneer.container.BrickLoadingException;
import sneer.container.NewContainer;

public class NewContainerImpl implements NewContainer {

	@Override
	public void runBrick(String classDirectory) throws IOException {
		try {
			tryToRunBrick(classDirectory);
		} catch (ClassNotFoundException e) {
			throw new BrickLoadingException(e);
		} catch (InstantiationException e) {
			throw new BrickLoadingException(e);
		} catch (NoSuchMethodException e) {
			throw new BrickLoadingException(e);
		} catch (InvocationTargetException e) {
			throw new BrickLoadingException(e);
		}
	}

	private void tryToRunBrick(String classDirectory)
			throws 
			InstantiationException,
			NoSuchMethodException, InvocationTargetException,
			IOException, ClassNotFoundException {
		
		String packageName = packageNameFor(classDirectory);
		File rootDirectory = computeRootDirectory(classDirectory, packageName);
		URLClassLoader classLoader = new URLClassLoader(new URL[] { toURL(rootDirectory) });
		Class<?> brickInterface = new BrickInterfaceFinder(classLoader, classDirectory, packageName).find();
		Class<?> brickImpl = classLoader.loadClass(implNameFor(brickInterface.getName()));
		newInstance(brickImpl);
		
	}

	private void newInstance(Class<?> brickImpl) throws NoSuchMethodException,
			InstantiationException,
			InvocationTargetException {
		try {
			Constructor<?> constructor = brickImpl.getDeclaredConstructor();
			constructor.setAccessible(true);
			constructor.newInstance();
		} catch (IllegalAccessException e) {
			throw new IllegalStateException();
		}
	}

	private URL toURL(File rootDirectory) {
		try {
			return rootDirectory.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

	private String packageNameFor(String classDirectory) throws IOException {
		return new PackageNameRetriever(classDirectory).retrieve();
	}

	private File computeRootDirectory(String classDirectory, String packageName) {
		return new File(classDirectory.substring(0, classDirectory.length() - packageName.length()));
	}
	
	private String implNameFor(final String brickInterfaceName) {
		return BrickConventions.implementationNameFor(brickInterfaceName);
	}

}
