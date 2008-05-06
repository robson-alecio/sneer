package sneer.bricks.classpath.impl;

import java.io.File;


/**
 * A single element containing the root directory where you can find .class files
 */
class DirectoryBasedClasspath extends ClasspathSupport {

	private File _rootFolder;
	
	public DirectoryBasedClasspath(File folder) {
		_rootFolder = folder;
		_elements.add(folder);
	}
	
	@Override
	public File relativeFile(Class<?> clazz) {
		return new File(fullNameFromClass(clazz));
	}

	@Override
	public File absoluteFile(Class<?> clazz) {
		File classFile = fileForClass(clazz);
		if(!classFile.exists()) {
			throw new IllegalArgumentException("Can't find class file for " + clazz);
		}
		return classFile;
	}

	private String fullNameFromClass(Class<?> clazz) {
		return clazz.getName().replaceAll("\\.", "/") + ".class";
	}

	private File fileForClass(Class<?> clazz) {
		File file = new File(_rootFolder, fullNameFromClass(clazz));
		return file;
	}
}
