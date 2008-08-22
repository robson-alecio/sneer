package sneer.kernel.container.jar.impl;

import java.io.File;

import sneer.kernel.container.jar.DeploymentJar;
import sneer.kernel.container.jar.DeploymentJarFactory;

public class DeploymentJarFactoryImpl implements DeploymentJarFactory {
	
	@Override
	public DeploymentJar create(File jarFile) {
		return new DeploymentJarImpl(jarFile);
	}

}
