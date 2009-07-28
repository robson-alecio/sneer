package sneer.bricks.software.code.metaclass;

import java.io.File;

import sneer.foundation.brickness.Brick;

@Brick
public interface MetaClasses {

	MetaClass metaClass(File rootFolder, File classFile);

	MetaClass metaClass(Class<?> clazz);

}
