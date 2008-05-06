package sneer.bricks.classpath;

import java.io.File;


public interface Classpath {

	String asJavacArgument();

	/**
	 * @param element is a single directory with .class files or jar file
	 */
	void add(File element);

	File absoluteFile(Class<?> clazz);

	File relativeFile(Class<?> clazz);

	Classpath compose(Classpath other);
}
