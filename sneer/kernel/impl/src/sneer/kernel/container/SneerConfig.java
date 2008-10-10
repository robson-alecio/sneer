package sneer.kernel.container;

import java.io.File;

public interface SneerConfig extends ContainerConfig {
	
	File sneerDirectory();
	
	File brickDirectory(Class<?> brickClass);

	File tmpDirectory(); 
}
