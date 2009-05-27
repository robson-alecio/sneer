package sneer.brickness.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;

import sneer.brickness.ClassDefinition;
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
		
		ClassDefinition originalClassDef = new ClassDefinition(name, toByteArray(classResource));
		List<ClassDefinition> classDefs = realizeNatures(originalClassDef);
		return defineClassesAndReturn(classDefs, name);
		
	}

	private Class<?> defineClassesAndReturn(List<ClassDefinition> classDefs, String classNameToReturn) throws ClassFormatError {
		Class<?> mainClass = null;
		for (ClassDefinition classDef : classDefs) {
			Class<?> clazz = defineClass(classDef.name, classDef.bytes, 0, classDef.bytes.length);
			if (classDef.name.equals(classNameToReturn)) {
				if (mainClass != null) 
					throw new IllegalStateException();
				mainClass = clazz;
			}
		}
		if (mainClass == null)
			throw new IllegalStateException();
		return mainClass;
	}

	private List<ClassDefinition> realizeNatures(ClassDefinition originalClassDef) {
		List<ClassDefinition> classDefs = Collections.singletonList(originalClassDef);
		for (Nature nature : _natures) {
			ArrayList<ClassDefinition> realized = new ArrayList<ClassDefinition>();
			for (ClassDefinition classDef : classDefs)
				realized.addAll(nature.realize(classDef));
			classDefs = realized;
		}
		return classDefs;
	}

	private byte[] toByteArray(final URL classResource) {
		try {
			return IOUtils.toByteArray(classResource.openStream());
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
