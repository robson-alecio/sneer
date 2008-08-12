package sneer.lego.jar.impl;

import java.io.File;

import sneer.lego.jar.SneerJar;
import sneer.lego.jar.SneerJarFactory;

public class SneerJarFactoryImpl implements SneerJarFactory {
	
	@Override
	public SneerJar create(File jarFile) {
		return new SneerJarImpl(jarFile);
	}

}
