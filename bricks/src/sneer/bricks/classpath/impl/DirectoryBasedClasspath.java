package sneer.bricks.classpath.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import sneer.lego.utils.io.JavaFilter;


/**
 * A single element containing the root directory where you can find .class files
 */
class DirectoryBasedClasspath extends ClasspathSupport {

	private File _rootFolder;
	
	private ClassLoader _classLoader;
	
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

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<Class<T>> findAssignableTo(Class<T> clazz) throws ClassNotFoundException {
		
		ClassLoader cl = classLoaderFromRootFolder();
		List<File> classFiles = classFiles();

		List<Class<T>> result = new ArrayList<Class<T>>();
		for (File classFile : classFiles) {
			String name = classNameForFile(classFile);
			Class<?> c = cl.loadClass(name);
			if(clazz.isAssignableFrom(c))  
				result.add((Class<T>) c);
		}
		return result;
	}

	private ClassLoader classLoaderFromRootFolder() {
		if(_classLoader != null)
			return _classLoader;
		
		try {
			_classLoader = new URLClassLoader(new URL[]{new URL("file://"+_rootFolder.getAbsolutePath()+"/")});
			return _classLoader;
		} catch (MalformedURLException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e);
		}
	}

	@Override
	public List<File> classFiles() {
		JavaFilter walker = new JavaFilter(_rootFolder);
		List<File> classFiles = walker.listFiles();
		return classFiles;
	}

	private String classNameForFile(File classFile) {
		String fileName = classFile.getAbsolutePath();
		String root = _rootFolder.getAbsolutePath();
		if(!fileName.startsWith(root)) /* silly check */ {
			throw new IllegalArgumentException(fileName + " not located inside "+root);
		}
		String result = fileName.substring(root.length() + 1);
		result = result.replaceAll("/", ".");
		result = result.substring(0,result.lastIndexOf("."));
		return result;
	}

}
