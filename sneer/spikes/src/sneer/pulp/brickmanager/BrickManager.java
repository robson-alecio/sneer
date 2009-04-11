package sneer.pulp.brickmanager;

import sneer.brickness.OldBrick;
import sneer.pulp.deployer.BrickBundle;
import sneer.pulp.deployer.BrickFile;
import sneer.pulp.reactive.collections.MapSignal;

public interface BrickManager extends OldBrick {

	BrickFile brick(String brickName);
	
	MapSignal<String, BrickFile> bricks();

	void install(BrickFile brick) throws BrickManagerException;
	
	void install(BrickBundle bundle) throws BrickManagerException;


}
