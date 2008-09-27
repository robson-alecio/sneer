package sneer.pulp.classpath;

import java.io.File;
import java.util.List;

public interface ClasspathFactory {

	Classpath newClasspath();

	Classpath fromClassDir(File folder);

	Classpath sneerApi();

	Classpath fromJarFiles(List<File> jarFiles);
}
