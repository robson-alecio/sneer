package sneer.bricks.pulp.events;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.foundation.lang.Consumer;

public interface EventSource<VO> extends PulseSource {

	Contract addReceiver(Consumer<? super VO> eventReceiver);

	void publicRemoveReceiver(Object eventReceiver);
}
