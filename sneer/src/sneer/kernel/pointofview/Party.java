package sneer.kernel.pointofview;

import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public interface Party {

	Signal<String> ownName();
	
	ListSignal<Contact> contacts();

	Signal<Boolean> isOnline();

}
