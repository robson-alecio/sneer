package sneer.bricks.pulp.events.pulsers;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.foundation.brickness.Brick;

@Brick
public interface Pulsers {
	
	Pulser newInstance();

	WeakContract receive(Runnable receiver, PulseSource... multipleSources);

}
