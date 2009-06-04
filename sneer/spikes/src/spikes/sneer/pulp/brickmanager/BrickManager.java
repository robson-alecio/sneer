package spikes.sneer.pulp.brickmanager;

import sneer.brickness.Brick;
import sneer.pulp.reactive.collections.MapSignal;
import spikes.sneer.pulp.deployer.BrickBundle;
import spikes.sneer.pulp.deployer.BrickFile;

@Brick
public interface BrickManager {

	BrickFile brick(String brickName);
	
	MapSignal<String, BrickFile> bricks();

	void install(BrickFile brick) throws BrickManagerException;
	
	void install(BrickBundle bundle) throws BrickManagerException;


}
