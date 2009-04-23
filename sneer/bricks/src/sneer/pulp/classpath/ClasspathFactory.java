package sneer.pulp.classpath;

import java.io.File;

import sneer.brickness.Brick;

@Brick
public interface ClasspathFactory {

	Classpath newClasspath();

	Classpath fromClassDir(File folder);

	Classpath sneerApi();

	Classpath fromJarFiles(File... jarFiles);
}
