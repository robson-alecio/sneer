package sneer.bricks.software.code.compilers.classpath;

import java.io.File;


public interface Classpath {

	String asJavacArgument();

	/**
	 * @param element is a single directory with .class files or jar file
	 */
	void add(File element);

	Classpath compose(Classpath other);
}
