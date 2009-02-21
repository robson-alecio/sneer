package sneer.pulp.classpath;

import java.io.File;
import java.util.List;

import sneer.kernel.container.Brick;

public interface ClasspathFactory extends Brick {

	Classpath newClasspath();

	Classpath fromClassDir(File folder);

	Classpath sneerApi();

	Classpath fromJarFiles(List<File> jarFiles);
}
