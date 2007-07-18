package sneer.kernel.communication;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface Channel {

	Omnivore<Packet> output();
	Signal<Packet> input();
	
	Signal<Integer> elementsInInputBuffer();
	
}
