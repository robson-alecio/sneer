package spikes.sneer.kernel.container;

import java.io.File;

import sneer.foundation.brickness.Brick;

@Brick
public interface SneerConfig extends ContainerConfig {
	
	File sneerDirectory();
	
	File brickDirectory(Class<?> brickClass);

	File tmpDirectory(); 
}
