package spikes.sneer.kernel.container;

import java.io.File;

public interface ClassLoaderFactory {

	ClassLoader sneerApi();

	ClassLoader produceBrickClassLoader(Class<?> brickClass, File brickDirectory);

	ClassLoader newApiClassLoader();
}
