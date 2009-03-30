package sneer.container.impl;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.io.FilenameUtils;

import sneer.container.BrickLoadingException;
import sneer.container.NewBrick;

class BrickInterfaceFinder {

	private String _classDirectory;
	private ClassLoader _classLoader;
	private String _packageName;

	BrickInterfaceFinder(String classDirectory, String packageName, ClassLoader classLoader) {
		_classLoader = classLoader;
		_classDirectory = classDirectory;
		_packageName = packageName;
	}

	Class<?> find() throws FileNotFoundException {
		Class<?> result = null;
		for (File classFile : listClassFiles()) {
			Class<?> candidate = loadClass(classFile);
			if (!candidate.isAnnotationPresent(NewBrick.class))
				continue;
			
			if (result != null)	throw new BrickLoadingException("More than one brick interface found in '" + _classDirectory + "'.");
			result = candidate;
		}

		if (result == null)
			throw new BrickLoadingException("No brick interface found in '" + _classDirectory + "'.");

		return result;
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
