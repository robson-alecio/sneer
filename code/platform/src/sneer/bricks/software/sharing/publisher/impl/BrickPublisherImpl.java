package sneer.bricks.software.sharing.publisher.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.software.bricks.finder.BrickFinder;
import sneer.bricks.software.sharing.publisher.BrickPublisher;
import sneer.bricks.software.sharing.publisher.BrickUsage;

class BrickPublisherImpl implements BrickPublisher {

	@Override
	public void publishAllBricks() throws IOException {
		for (String brickName : my(BrickFinder.class).findBricks())
			publishBrick(brickName);
	}

	private void publishBrick(String brickName) {
		my(TupleSpace.class).publish(new BrickUsage(brickName));
	}

}
