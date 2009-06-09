package sneer.software.code.metaclass.impl;

import static sneer.commons.environments.Environments.my;

import java.io.File;

import sneer.software.code.classutils.ClassUtils;
import sneer.software.code.metaclass.MetaClass;
import sneer.software.code.metaclass.MetaClasses;

class MetaClassesImpl implements MetaClasses {

	@Override
	public MetaClass metaClass(File rootDirectory, File classFile) {
	    return new SimpleMetaClass(rootDirectory, classFile);
	}

	@Override
	public MetaClass metaClass(Class<?> clazz) {
	    return metaClass(my(ClassUtils.class).rootDirectoryFor(clazz), my(ClassUtils.class).toFile(clazz));
	}
}
