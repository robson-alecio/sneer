package sneer.pulp.mesh;

import sneer.kernel.container.Brick;
import sneer.pulp.contacts.Contact;
import wheel.reactive.lists.ListSignal;

public interface Party {

	ListSignal<Contact> contacts();
	Party navigateTo(Contact contact);
	
	<B extends Brick> B brickProxyFor(Class<B> brickInterface); 
}
