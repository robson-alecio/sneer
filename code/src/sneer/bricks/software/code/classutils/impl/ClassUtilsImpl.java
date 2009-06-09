package sneer.bricks.software.code.classutils.impl;

import java.io.File;
import java.net.URISyntaxException;

import sneer.bricks.software.code.classutils.ClassUtils;

class ClassUtilsImpl implements ClassUtils {

	@Override
	public File rootDirectoryFor(Class<?> clazz) {
		return rootDirectoryFor(clazz, toFile(clazz));
	}

	@Override
	public File toFile(Class<?> clazz) {
		try {
			return new File(clazz.getResource(clazz.getSimpleName() + ".class").toURI());
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}

	private File rootDirectoryFor(Class<?> clazz, File classFile) {
		final int packageCount = packageName(clazz).split("\\.").length;

		File parent = classFile.getParentFile();
		for (int i=0; i<packageCount; ++i)
			parent = parent.getParentFile();

		return parent;
	}

	private String packageName(Class<?> clazz) {
		return clazz.getPackage().getName();
	}
}