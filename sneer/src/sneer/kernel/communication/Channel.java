package sneer.kernel.communication;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface Channel {

	Omnivore<Packet> output();
	Signal<Packet> input();
	
	Signal<Integer> elementsInInputBuffer(); //Refactor: Remove this from the channel and create a general-purpose Buffer to be plugged onto any signal.
	
}
