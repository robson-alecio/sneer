package sneer.brickness.impl;

import java.io.File;

/** Loads only classes from a given package and its subpackages. All other loads are sent to a delegate.*/
class ClassLoaderForPackage extends FilteredClassLoader {	

	private final String _package;

	public ClassLoaderForPackage(File classRootDirectory, String packageName,
			ClassLoader delegateForOtherPackages) {
		super(classRootDirectory, delegateForOtherPackages);
		_package = packageName;
	}

	@Override
	protected boolean accept(String className) {
		return className.startsWith(_package);
	}

}
