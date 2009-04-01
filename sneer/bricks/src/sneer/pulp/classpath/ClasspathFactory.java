package sneer.pulp.classpath;

import java.io.File;

import sneer.brickness.OldBrick;

public interface ClasspathFactory extends OldBrick {

	Classpath newClasspath();

	Classpath fromClassDir(File folder);

	Classpath sneerApi();

	Classpath fromJarFiles(File... jarFiles);
}
