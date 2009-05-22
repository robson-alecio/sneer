package sneer.software.code.metaclass;

import java.io.File;

import sneer.brickness.Brick;

@Brick
public interface MetaClasses {

	MetaClass metaClass(File rootDirectory, File classFile);

	MetaClass metaClass(Class<?> clazz);

}
