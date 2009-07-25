package sneer.bricks.pulp.events;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.pulp.events.pulsers.PulseSource;
import sneer.foundation.lang.Consumer;

public interface EventSource<VO> extends PulseSource {

	WeakContract addReceiver(Consumer<? super VO> eventReceiver);
	
}
