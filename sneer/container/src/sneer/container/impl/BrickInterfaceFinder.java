package sneer.container.impl;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.io.FilenameUtils;

import sneer.container.NewBrick;

public class BrickInterfaceFinder {

	private String _classDirectory;
	private ClassLoader _classLoader;
	private String _packageName;

	public BrickInterfaceFinder(ClassLoader classLoader, String classDirectory, String packageName) {
		_classLoader = classLoader;
		_classDirectory = classDirectory;
		_packageName = packageName;
	}

	public Class<?> find() throws FileNotFoundException {
		for (File classFile : listClassFiles()) {
			Class<?> klass = loadClass(classFile);
			if (klass.isAnnotationPresent(NewBrick.class))
				return klass;
		}
		return null;
	}
	
	private Class<?> loadClass(File classFile) {
		try {
			final String className = FilenameUtils.getBaseName(classFile.getName());
			return _classLoader.loadClass(_packageName + "." + className);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException();
		}
	}

	private File[] listClassFiles() throws FileNotFoundException {
		return ClassFiles.list(_classDirectory);
	}

}
