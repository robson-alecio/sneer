package sneer.bricks.classpath;

import java.io.File;
import java.util.List;


public interface Classpath {

	String asJavacArgument();

	/**
	 * @param element is a single directory with .class files or jar file
	 */
	void addElement(File element);

	<T> List<Class<T>> findAssignableTo(Class<T> clazz) 
		throws ClassNotFoundException;

	File absoluteFile(Class<?> clazz);

	File relativeFile(Class<?> clazz);

	Classpath compose(Classpath other);
	
	List<File> classFiles();
}
