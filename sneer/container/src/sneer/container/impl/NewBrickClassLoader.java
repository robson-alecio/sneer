package sneer.container.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import sneer.brickness.environment.BrickConventions;

class NewBrickClassLoader extends URLClassLoader {

	private final String _implPackageName;
	private final ClassLoader _apiClassLoader;

	NewBrickClassLoader(String classDirectory, String packageName, ClassLoader apiClassLoader) {
		super(classpathFor(classDirectory, packageName), null); //No parent.
		_implPackageName = BrickConventions.implPackageFor(packageName);
		_apiClassLoader = apiClassLoader;
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		if (resolve) throw new IllegalStateException(); //Since we have no subclasses, this should never happen.
		
		Class<?> loaded = findLoadedClass(name);
		if (loaded != null) return loaded;
		
		return name.startsWith(_implPackageName)
			? super.loadClass(name, false)
			: _apiClassLoader.loadClass(name);
	}

	
	static private URL toURL(File file) {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

	private static URL[] classpathFor(String classDirectory, String packageName) {
		return new URL[]{toURL(classpathRootFor(classDirectory, packageName))};
	}

	static private File classpathRootFor(String classDirectory, String packageName) {
		return new File(classDirectory.substring(0, classDirectory.length() - packageName.length()));
	}

}
