package sneer.kernel.container.jar.impl;

import java.io.File;

import sneer.kernel.container.jar.SneerJar;
import sneer.kernel.container.jar.SneerJarFactory;

public class SneerJarFactoryImpl implements SneerJarFactory {
	
	@Override
	public SneerJar create(File jarFile) {
		return new SneerJarImpl(jarFile);
	}

}
