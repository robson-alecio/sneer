package sneer.container.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import sneer.brickness.environment.BrickConventions;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.lang.ByRef;
import sneer.container.BrickLoadingException;
import sneer.container.NewContainer;

public class NewContainerImpl implements NewContainer {
	
	private final List<Object> _bricks = new ArrayList<Object>();
	private final Environment _environment = new Environment() { @Override public <T> T provide(Class<T> intrface) {
		for (Object brick : _bricks)
			if (intrface.isInstance(brick))
				return (T)brick;
		return null;
	}};

	@Override
	public void runBrick(final String classDirectory) throws IOException {
		try {
			tryToRunBrick(classDirectory);
		} catch (ClassNotFoundException e) {
			throw new BrickLoadingException(e);
		}
	}

	private void tryToRunBrick(String classDirectory)
			throws
			IOException, ClassNotFoundException {
		
		String packageName = packageNameFor(classDirectory);
		File rootDirectory = computeRootDirectory(classDirectory, packageName);
		URLClassLoader classLoader = new URLClassLoader(new URL[] { toURL(rootDirectory) });
		Class<?> brickInterface = new BrickInterfaceFinder(classLoader, classDirectory, packageName).find();
		if (brickInterface == null)
			throw new BrickLoadingException("No brick interface found in '" + classDirectory + "'.");
		final Class<?> brickImpl = classLoader.loadClass(implNameFor(brickInterface.getName()));
		bind(newInstanceInEnvironment(brickImpl));
	}
	
	private Object newInstanceInEnvironment(final Class<?> brickImpl) {
		final ByRef<Object> result = ByRef.newInstance();
		Environments.runWith(_environment, new Runnable() { @Override public void run() {
			try {
				result.value = newInstance(brickImpl);
			} catch (InstantiationException e) {
				throw new BrickLoadingException(e);
			} catch (NoSuchMethodException e) {
				throw new BrickLoadingException(e);
			} catch (InvocationTargetException e) {
				throw new BrickLoadingException(e);
			}
		}});
		return result.value;
	}

	private void bind(Object newBrickInstance) {
		_bricks.add(newBrickInstance);
	}

	private Object newInstance(Class<?> brickImpl) throws NoSuchMethodException,
			InstantiationException,
			InvocationTargetException {
		try {
			Constructor<?> constructor = brickImpl.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
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
