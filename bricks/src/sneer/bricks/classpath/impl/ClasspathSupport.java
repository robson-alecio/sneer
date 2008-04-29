package sneer.bricks.classpath.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import sneer.bricks.classpath.Classpath;

abstract public class ClasspathSupport implements Classpath {

	protected static final File RT_JAR = new File(System.getProperty("java.home")+File.separator+"lib"+File.separator+"rt.jar"); 

	protected List<File> _elements = new ArrayList<File>();
	
	@Override
	public String asJavacArgument() {
		StringBuffer sb = new StringBuffer();
		for (File entry : _elements) {
			sb.append(entry.getAbsolutePath());
			if(entry.isDirectory()) sb.append('/');
			sb.append(File.pathSeparatorChar);
		}
		String result = sb.toString();
		result = StringUtils.chomp(result, File.pathSeparatorChar+"");
		return result;
	}

	@Override
	public void addElement(File element) {
		_elements.add(element);
	}

	@Override
	public File absoluteFile(Class<?> clazz) {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public File relativeFile(Class<?> clazz) {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public Classpath compose(Classpath other) {
		return new ComposedClasspath(this, other);
	}
	

	@Override
	public List<File> classFiles() {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@SuppressWarnings("unused")
	@Override
	public <T> List<Class<T>> findAssignableTo(Class<T> clazz) throws ClassNotFoundException {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}
}
