package sneer.container.impl;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.io.FilenameUtils;

import sneer.brickness.OldBrick;
import sneer.container.BrickLoadingException;
import sneer.container.Brick;
import sneer.skin.GuiBrick;
import sneer.skin.snappmanager.Instrument;

class BrickInterfaceSearch {

	private final String _classDirectory;
	private final ClassLoader _classLoader;
	private final String _packageName;
	
	BrickInterfaceSearch(String classDirectory, String packageName, ClassLoader classLoader) {
		_classLoader = classLoader;
		_classDirectory = classDirectory;
		_packageName = packageName;
	}

	Class<?> result() throws FileNotFoundException {
		Class<?> result = null;
		for (File classFile : listClassFiles()) {
			Class<?> candidate = loadClass(classFile);
			if (!candidate.isAnnotationPresent(Brick.class) && !OldBrick.class.isAssignableFrom(candidate))
				continue;
			if (candidate.getName().equals(Instrument.class.getName()))
				continue;
			if (candidate.getName().equals(GuiBrick.class.getName()))
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
