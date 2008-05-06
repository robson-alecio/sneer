package sneer.bricks.brickmanager;

import sneer.bricks.deployer.BrickBundle;

public interface BrickManager {

	void install(BrickBundle bundle) throws BrickManagerException;

}
