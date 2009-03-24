package sneer.pulp.brickmanager;

import sneer.brickness.Brick;
import sneer.pulp.deployer.BrickBundle;
import sneer.pulp.deployer.BrickFile;
import wheel.reactive.maps.MapSignal;

public interface BrickManager extends Brick {

	BrickFile brick(String brickName);
	
	MapSignal<String, BrickFile> bricks();

	void install(BrickFile brick) throws BrickManagerException;
	
	void install(BrickBundle bundle) throws BrickManagerException;


}
