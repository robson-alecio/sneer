package sneer.bricks.brickmanager;

import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.BrickFile;
import sneer.lego.Brick;
import wheel.reactive.maps.MapSignal;

public interface BrickManager extends Brick {

	BrickFile brick(String brickName);
	MapSignal<String, BrickFile> bricks();

	void install(BrickFile brick) throws BrickManagerException;
	
	void install(BrickBundle bundle) throws BrickManagerException;


}
