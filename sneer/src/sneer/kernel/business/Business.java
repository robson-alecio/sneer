package sneer.kernel.business;

import wheel.reactive.Signal;
import wheel.reactive.list.ListSignal;

public interface Business {

	Signal<String> ownName();
	
	Signal<Integer> sneerPort();

	ListSignal<Contact> contacts();

}
