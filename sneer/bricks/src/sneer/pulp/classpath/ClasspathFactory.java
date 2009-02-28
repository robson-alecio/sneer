package sneer.pulp.classpath;

import java.io.File;

import sneer.brickness.Brick;

public interface ClasspathFactory extends Brick {

	Classpath newClasspath();

	Classpath fromClassDir(File folder);

	Classpath sneerApi();

	Classpath fromJarFiles(File... jarFiles);
}
