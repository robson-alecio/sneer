package sneer.bricks.deployer;

import java.io.File;

public interface Deployer {
	
	/**
	 * packages a local version of a brick that can be installed locally 
	 */
	BrickBundle pack(File path) throws DeployerException;

}
