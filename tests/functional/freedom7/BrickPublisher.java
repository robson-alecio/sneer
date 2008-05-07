package functional.freedom7;

import java.io.File;

import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.BrickFile;
import functional.SovereignParty;

public interface BrickPublisher extends SovereignParty {
	
	BrickBundle publishBrick(File sourceDirectory);

	void meToo(BrickPublisher party, String brickName);

	BrickFile brick(String brickName);

	Object produce(String brickName);
}
