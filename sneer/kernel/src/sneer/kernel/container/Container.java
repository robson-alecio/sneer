package sneer.kernel.container;

import sneer.brickness.Brick;
import sneer.commons.environments.Environment;

public interface Container extends Environment {
	
	Class<? extends Brick> resolve(String brickName) throws ClassNotFoundException;

}