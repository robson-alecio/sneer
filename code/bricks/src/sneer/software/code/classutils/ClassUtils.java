package sneer.software.code.classutils;

import java.io.File;

import sneer.brickness.Brick;

@Brick
public interface ClassUtils {

	File rootDirectoryFor(Class<?> clazz);

	File toFile(Class<?> clazz);

}
