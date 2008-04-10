package sneer.bricks.classpath.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sneer.bricks.classpath.Classpath;

class ComposedClasspath implements Classpath {

	private Classpath _cp1;
	
	private Classpath _cp2;
	
	public ComposedClasspath(Classpath cp1, Classpath cp2) {
		if(cp1 == null || cp2 == null)
			throw new IllegalArgumentException("Can't compose classpaths");
		
		_cp1 = cp1;
		_cp2 = cp2;
	}

	@Override
	public String asJavacArgument() {
		String fromCp1 = _cp1.asJavacArgument();
		String fromCp2 = _cp2.asJavacArgument();
		return fromCp1 + File.pathSeparator + fromCp2;
	}

	@Override
	public Classpath compose(Classpath other) {
		return new ComposedClasspath(this, other);
	}

	@Override
	public <T> List<Class<T>> findAssignableTo(Class<T> clazz) throws ClassNotFoundException {
		List<Class<T>> result = new ArrayList<Class<T>>();
		result.addAll(_cp1.findAssignableTo(clazz));
		result.addAll(_cp2.findAssignableTo(clazz));
		return result;
	}

	@Override
	public File relativeFile(Class<?> clazz) {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public File absoluteFile(Class<?> clazz) {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void addElement(File element) {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

}
