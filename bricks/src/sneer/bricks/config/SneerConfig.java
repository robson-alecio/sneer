package sneer.bricks.config;

import java.io.File;

public interface SneerConfig {
	
	File sneerDirectory();
	
	File brickRootDirectory();
	
	/* 
	 * used for development. Error prone !! 
	 */
	File eclipseDirectory();

	File tmpDirectory(); 
}
