package sneer.bricks.pulp.events;

import sneer.foundation.lang.Consumer;

public interface EventSource<VO> extends PulseSource {

	void addReceiver(Consumer<? super VO> eventReceiver);
	void removeReceiver(Object eventReceiver);
}
