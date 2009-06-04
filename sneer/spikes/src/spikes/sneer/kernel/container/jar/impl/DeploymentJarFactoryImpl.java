package spikes.sneer.kernel.container.jar.impl;

import java.io.File;

import spikes.sneer.kernel.container.jar.DeploymentJar;
import spikes.sneer.kernel.container.jar.DeploymentJarFactory;

public class DeploymentJarFactoryImpl implements DeploymentJarFactory {
	
	@Override
	public DeploymentJar create(File jarFile) {
		return new DeploymentJarImpl(jarFile);
	}

}
