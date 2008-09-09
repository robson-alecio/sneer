package sneer.kernel.container;

import java.io.File;

public interface SneerConfig {
	
	File sneerDirectory();
	
	File brickRootDirectory();
	
	File brickDirectory(Class<?> brickClass);
	
	/* 
	 * used for development. Error prone !! 
	 */
	File eclipseDirectory();

	File tmpDirectory(); 
}
