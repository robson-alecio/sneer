package sneer.bricks.software.code.compilers.classpath;

import java.io.File;

import sneer.foundation.brickness.Brick;

@Brick
public interface ClasspathFactory {

	Classpath newClasspath();

	Classpath fromClassDir(File folder);

	Classpath sneerApi();

	Classpath fromJarFiles(File... jarFiles);
}
