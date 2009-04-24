package sneer.software.compilers.classpath.impl;

import java.io.File;


/**
 * A single element containing the root directory where you can find .class files
 */
class DirectoryBasedClasspath extends ClasspathSupport {

	public DirectoryBasedClasspath(File folder) {
		_elements.add(folder);
	}
}
