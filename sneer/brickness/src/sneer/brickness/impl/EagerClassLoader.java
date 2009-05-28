package sneer.brickness.impl;

import java.net.URL;
import java.net.URLClassLoader;

/** Does not have a parent, so it has first pick on the classes it wants to load. Defers to the "next" classLoader to load the classes it doesn't want to load.*/
abstract class EagerClassLoader extends URLClassLoader {

	protected final ClassLoader _next;

	protected EagerClassLoader(URL[] urls, ClassLoader next) {
		super(urls, null); //No parent.
		_next = next;
	}

	protected abstract boolean isEagerToLoad(String className);


	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		if (resolve) throw new IllegalStateException();
		
		Class<?> loaded = findLoadedClass(name);
		if (loaded != null) return loaded;
		
		return isEagerToLoad(name)
			? doLoadClass(name)
			: _next.loadClass(name);
	}

	protected Class<?> doLoadClass(String name) throws ClassNotFoundException {
		return super.loadClass(name, false);
	}


}