package sneer.bricks.software.sharing;

import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.foundation.brickness.Brick;

@Brick
public interface BrickUniverse {

	SetSignal<BrickInfo> availableBricks();
	
}
