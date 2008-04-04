package sneer.bricks.deployer;

import java.io.File;
import java.util.List;

public interface Deployer {
	
	List<String> list();
	
	/**
	 * packages a local version of a brick suitable for another party to import/deploy 
	 */
	BrickFile pack(File path) throws DeployerException;

	/**
	 * deploys locally a brick packaged by pack(); 
	 */
	void deploy(BrickFile file) throws DeployerException;
	
}
