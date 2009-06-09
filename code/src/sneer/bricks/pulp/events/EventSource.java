package sneer.bricks.pulp.events;

import sneer.bricks.hardware.cpu.lang.Consumer;

public interface EventSource<VO> {

	void addReceiver(Consumer<? super VO> receiver);
	void removeReceiver(Object receiver);
}
