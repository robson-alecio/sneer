package sneer.pulp.events;

import sneer.hardware.cpu.lang.Consumer;

public interface EventSource<VO> {

	void addReceiver(Consumer<? super VO> receiver);
	void removeReceiver(Object receiver);
}
