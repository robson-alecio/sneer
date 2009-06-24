package sneer.bricks.software.code.metaclass.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;

import sneer.bricks.software.code.classutils.ClassUtils;
import sneer.bricks.software.code.metaclass.MetaClass;
import sneer.bricks.software.code.metaclass.MetaClasses;

class MetaClassesImpl implements MetaClasses {

	@Override
	public MetaClass metaClass(File rootDirectory, File classFile) {
	    return new SimpleMetaClass(rootDirectory, classFile);
	}

	@Override
	public MetaClass metaClass(Class<?> clazz) {
	    return metaClass(my(ClassUtils.class).classpathRootFor(clazz), my(ClassUtils.class).toFile(clazz));
	}
}
