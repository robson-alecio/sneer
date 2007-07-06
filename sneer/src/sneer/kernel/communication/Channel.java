package sneer.kernel.communication;

import sneer.kernel.business.contacts.ContactId;
import wheel.io.Connection;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface Channel {

	Omnivore<Packet> output();
	Signal<Packet> input();
	
}
