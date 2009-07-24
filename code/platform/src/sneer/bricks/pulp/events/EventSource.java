package sneer.bricks.pulp.events;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.foundation.lang.Consumer;

public interface EventSource<VO> extends PulseSource {

	Contract addReceiverWithContract(Consumer<? super VO> eventReceiver);

	void addReceiver(Consumer<? super VO> eventReceiver);
	void removeReceiver(Object eventReceiver);
}
