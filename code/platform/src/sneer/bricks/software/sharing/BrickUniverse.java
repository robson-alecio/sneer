package sneer.bricks.software.sharing;

import sneer.bricks.pulp.reactive.collections.SetSignal;

public interface BrickUniverse {

	SetSignal<BrickInfo> availableBricks();
	
}
