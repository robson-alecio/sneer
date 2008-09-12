package sneer.pulp.classpath.impl;

import java.io.File;
import java.util.List;

/**
 * Elements are .jar files
 */
class JarBasedClasspath extends ClasspathSupport {

	public JarBasedClasspath(File jarFile) {
		add(jarFile);
	}

	public JarBasedClasspath(List<File> jarFiles) {
		for (File file : jarFiles) {
			add(file);
		}
	}
}
