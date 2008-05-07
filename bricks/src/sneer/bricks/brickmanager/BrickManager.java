package sneer.bricks.brickmanager;

import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.BrickFile;

public interface BrickManager {

	BrickFile brick(String brickName);

	void install(BrickFile brick) throws BrickManagerException;
	
	void install(BrickBundle bundle) throws BrickManagerException;

}
