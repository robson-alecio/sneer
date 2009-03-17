package sneer.pulp.events;

import wheel.lang.Consumer;

public interface EventSource<VO> {

	void addReceiver(Consumer<? super VO> receiver);
	void removeReceiver(Object receiver);
}
