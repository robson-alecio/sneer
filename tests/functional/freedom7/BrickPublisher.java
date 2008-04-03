package functional.freedom7;

import java.io.File;

import functional.SovereignParty;

public interface BrickPublisher extends SovereignParty {
	
	BrickPublished publishBrick(File brickDirectory);

	void meToo(String interface1);

	
}
