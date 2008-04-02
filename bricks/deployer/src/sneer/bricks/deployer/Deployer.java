package sneer.bricks.deployer;

import java.io.File;
import java.util.List;

public interface Deployer {
	
	List<String> list();
	
	/**
	 * packages a local version of a brick suitable suitable for another peer to import/deploy 
	 */
	BrickFile pack(File path, String brickName, String version) throws DeployerException;

	/**
	 * deploys a brick packaged by export(); 
	 */
	void deploy(BrickFile file) throws DeployerException;
	
}
