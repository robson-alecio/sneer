package sneer.bricks.classpath;

import java.io.File;

public interface ClasspathFactory {

	Classpath newClasspath();

	Classpath fromLibDir(File folder);

	Classpath fromDirectory(File folder);

	Classpath sneerApi();
}
