/**
 * 
 */
package sneer.brickness.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ClassUtils;

final class ApiClassLoader extends FilteredClassLoader {

	private final Set<String> _packages = new HashSet<String>();
	
	public ApiClassLoader(ClassLoader parent) {
		super(new URL[0], parent, parent);
	}

	@Override
	protected boolean accept(String className) {
		String packageName = ClassUtils.getPackageName(className);
		if (packageName.isEmpty())
			return false;
		if (_packages.contains(packageName))
			return true;
		return accept(ClassUtils.getPackageName(packageName));
	}

	public void add(File classRootDirectory, String brickName) {
		try {
			addURL(classRootDirectory.toURI().toURL());
			_packages .add(ClassUtils.getPackageName(brickName));
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}
}