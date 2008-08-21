package sneer.kernel.container.jar;

import java.io.File;


public interface SneerJarFactory {

	SneerJar create(File jarFile);

}
