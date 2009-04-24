package sneer.brickness.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public abstract class FilteredClassLoader extends URLClassLoader {

	protected final ClassLoader _delegate;

	public static URL[] toURL(File file) {
		try {
			return new URL[]{file.toURI().toURL()};
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

	protected abstract boolean accept(String className);

	protected FilteredClassLoader(File classRootDirectory, ClassLoader delegateForOtherPackages) {
		this(toURL(classRootDirectory), null /* no parent */, delegateForOtherPackages);
	}

	protected FilteredClassLoader(URL[] urls, ClassLoader parent, ClassLoader delegateForOtherPackages) {
		super(urls, parent);
		_delegate = delegateForOtherPackages;
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		if (resolve) throw new IllegalStateException(); //Since we have no subclasses, this should never happen.
		
		Class<?> loaded = findLoadedClass(name);
		if (loaded != null) return loaded;
		
		return accept(name)
			? doLoadClass(name)
			: _delegate.loadClass(name);
	}

	protected Class<?> doLoadClass(String name) throws ClassNotFoundException {
		return super.loadClass(name, false);
	}

}