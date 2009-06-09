package sneer.bricks.software.code.classutils;

import java.io.File;

import sneer.foundation.brickness.Brick;

@Brick
public interface ClassUtils {

	File rootDirectoryFor(Class<?> clazz);

	File toFile(Class<?> clazz);

}
