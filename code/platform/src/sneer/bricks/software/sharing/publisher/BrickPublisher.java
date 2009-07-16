package sneer.bricks.software.sharing.publisher;

import java.io.IOException;

import sneer.foundation.brickness.Brick;

@Brick
public interface BrickPublisher {

	void publishAllBricks() throws IOException;

}
