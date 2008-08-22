package sneer.kernel.container;

import java.io.File;

public interface ClassLoaderFactory {

	ClassLoader sneerApi();

//	ApiClassLoader apiClassLoader();
	 
	ClassLoader produceBrickClassLoader(Class<?> brickClass, File brickDirectory);
}
