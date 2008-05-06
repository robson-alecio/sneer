package sneer.bricks.classpath;

import java.io.File;
import java.util.List;

public interface ClasspathFactory {

	Classpath newClasspath();

	Classpath fromLibDir(File folder);

	Classpath fromDirectory(File folder);

	Classpath sneerApi();

	Classpath fromJarFiles(List<File> jarFiles);
}
