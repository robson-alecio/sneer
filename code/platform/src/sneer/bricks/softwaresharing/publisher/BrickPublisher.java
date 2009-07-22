package sneer.bricks.softwaresharing.publisher;

import java.io.IOException;

import sneer.foundation.brickness.Brick;

@Brick
public interface BrickPublisher {

	void publishBrick(String brickName) throws IOException;

}
