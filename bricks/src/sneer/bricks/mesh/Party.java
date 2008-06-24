package sneer.bricks.mesh;

import sneer.bricks.contacts.Contact;
import sneer.lego.Brick;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public interface Party {

	ListSignal<Contact> contacts();
	Party navigateTo(Contact contact);
	
	<S> Signal<S> signal(String signalPath);

	<B extends Brick> B brickProxyFor(Class<B> brickInterface); 

}
