package sneer.bricks.software.sharing;

import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.bricks.software.bricks.snappstarter.Snapp;
import sneer.foundation.brickness.Brick;

@Snapp
@Brick
public interface BrickUniverse {

	SetSignal<BrickInfo> availableBricks();
	
}
