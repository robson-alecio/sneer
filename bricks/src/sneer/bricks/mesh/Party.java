package sneer.bricks.mesh;

import sneer.bricks.contacts.Contact;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.maps.MapSignal;

public interface Party {

	ListSignal<Contact> contacts();
	Party navigateTo(Contact contact);
	
	<S> Signal<S> signal(String signalPath);
	<K,V> MapSignal<K, V> mapSignal(String signalPath);

}
