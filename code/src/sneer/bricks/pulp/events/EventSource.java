package sneer.bricks.pulp.events;

import sneer.foundation.lang.Consumer;

public interface EventSource<VO> {

	void addReceiver(Consumer<? super VO> receiver);
	void removeReceiver(Object receiver);
}
