package sneer.brickness.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;

import sneer.brickness.Nature;

final class ClassLoaderForPackageWithNatures extends ClassLoaderForPackage {

	private final List<Nature> _natures;

	ClassLoaderForPackageWithNatures(File classRootDirectory,
			String packageName, ClassLoader delegateForOtherPackages, List<Nature> natures) {
		super(classRootDirectory, packageName, delegateForOtherPackages);
		_natures = natures;
	}
	
	@Override
	protected Class<?> doLoadClass(String name) throws ClassNotFoundException {
		
		final URL classResource = findResource(name.replace('.', '/') + ".class");
		if (classResource == null)
			throw new ClassNotFoundException(name);
		
		byte[] classBytes = toByteArray(classResource);
		for (Nature nature : _natures)
			classBytes = nature.realize(classBytes);
		
		return defineClass(name, classBytes, 0, classBytes.length);
		
	}

	private byte[] toByteArray(final URL classResource) {
		try {
			return IOUtils.toByteArray(classResource.openStream());
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
