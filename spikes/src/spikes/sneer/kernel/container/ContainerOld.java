package spikes.sneer.kernel.container;

import sneer.commons.environments.Environment;

public interface ContainerOld extends Environment {
	
	Class<?> resolve(String brickName) throws ClassNotFoundException;

}