package sneer.kernel.container;

import sneer.brickness.OldBrick;
import sneer.commons.environments.Environment;

public interface ContainerOld extends Environment {
	
	Class<? extends OldBrick> resolve(String brickName) throws ClassNotFoundException;

}