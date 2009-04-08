package sneer.brickness.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/** Loads only classes from a given package and its subpackages. All other loads are sent to a delegate.*/
class ClassLoaderForPackage extends URLClassLoader {

	private final String _package;
	private final ClassLoader _delegate;

	ClassLoaderForPackage(File classRootDirectory, String packageName, ClassLoader delegateForOtherPackages) {
		super(toURL(classRootDirectory), null); //No parent.
		_package = packageName;
		_delegate = delegateForOtherPackages;
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		if (resolve) throw new IllegalStateException(); //Since we have no subclasses, this should never happen.
		
		Class<?> loaded = findLoadedClass(name);
		if (loaded != null) return loaded;
		
		return name.startsWith(_package)
			? doLoadClass(name)
			: _delegate.loadClass(name);
	}

	protected Class<?> doLoadClass(String name) throws ClassNotFoundException {
		return super.loadClass(name, false);
	}
	
	static private URL[] toURL(File file) {
		try {
			return new URL[]{file.toURI().toURL()};
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

}
