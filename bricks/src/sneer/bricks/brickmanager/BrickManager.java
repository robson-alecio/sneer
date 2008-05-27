package sneer.bricks.brickmanager;

import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.BrickFile;
import wheel.reactive.maps.MapSignal;

public interface BrickManager {

	BrickFile brick(String brickName);
	MapSignal<String, BrickFile> bricks();

	void install(BrickFile brick) throws BrickManagerException;
	
	void install(BrickBundle bundle) throws BrickManagerException;


}
