package sneer.bricks.software.code.compilers.classpath.impl;

import java.io.File;


/**
 * A single element containing the root folder where you can find .class files
 */
class FolderBasedClasspath extends ClasspathSupport {

	public FolderBasedClasspath(File folder) {
		_elements.add(folder);
	}
}
