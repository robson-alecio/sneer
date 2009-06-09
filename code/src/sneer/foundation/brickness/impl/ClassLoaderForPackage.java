package sneer.foundation.brickness.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import sneer.foundation.brickness.Nature;

/** Loads only classes from a given package and its subpackages. All other loads are deferred to the next classLoader.*/
class ClassLoaderForPackage extends ClassLoaderWithNatures {	

	public ClassLoaderForPackage(File classpath, String packageName, List<Nature> natures, ClassLoader next) {
		super(toURLs(classpath), next, natures);
		_package = packageName;
	}

	
	private final String _package;

		
	@Override
	protected boolean isEagerToLoad(String className) {
		return className.startsWith(_package);
	}

	private static URL[] toURLs(File file) {
		try {
			return new URL[]{file.toURI().toURL()};
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

}
