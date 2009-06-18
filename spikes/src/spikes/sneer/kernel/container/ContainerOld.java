package spikes.sneer.kernel.container;

import sneer.foundation.environments.Environment;

public interface ContainerOld extends Environment {
	
	Class<?> resolve(String brickName) throws ClassNotFoundException;

}