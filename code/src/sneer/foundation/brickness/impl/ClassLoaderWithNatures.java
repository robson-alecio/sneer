package sneer.foundation.brickness.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sneer.foundation.brickness.ClassDefinition;
import sneer.foundation.brickness.Nature;

abstract class ClassLoaderWithNatures extends EagerClassLoader {

	ClassLoaderWithNatures(URL[] urls, ClassLoader next, List<Nature> natures) {
		super(urls, next);
		_natures = natures;
	}
	
	private final List<Nature> _natures;
	
	@Override
	protected Class<?> doLoadClass(String name) throws ClassNotFoundException {
		
		final URL classResource = findResource(name.replace('.', '/') + ".class");
		if (classResource == null) throw new ClassNotFoundException(name);
		
		ClassDefinition originalClassDef;
		try {
			originalClassDef = new ClassDefinition(name, toByteArray(classResource));
		} catch (IOException e) {
			throw new ClassNotFoundException("Invalid URL (" + classResource +")", e);
		}
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
		if (mainClass == null)throw new IllegalStateException();
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

    public byte[] toByteArray(final URL classResource) throws IOException {
    	InputStream input = classResource.openStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024*4];
		int n = 0;
		while (-1 != (n = input.read(buffer))) 
			output.write(buffer, 0, n);
        return output.toByteArray();
    }
}