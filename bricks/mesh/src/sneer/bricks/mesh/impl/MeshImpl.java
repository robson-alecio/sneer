package sneer.bricks.mesh.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.mesh.Mesh;
import sneer.contacts.Contact;
import sneer.contacts.ContactManager;
import sneer.lego.Brick;
import sneer.lego.Injector;
import wheel.reactive.Signal;

public class MeshImpl implements Mesh {

	@Brick
	private ContactManager _contactManager;

	@Brick
	private Injector _injector;

	private final Map<Contact, ContactProxy> _proxiesByContact = new HashMap<Contact, ContactProxy>();


	@Override
	public <T> Signal<T> findSignal(String nicknamePath, String signalPath) {
		String[] path = nicknamePath.split("/", 1);
		String head = path[0];
		String tail = path.length > 1
			? path[1]
			: "";
			
		Contact immediateContact = _contactManager.contactGiven(head);
		ContactProxy proxy = produceProxyFor(immediateContact);
		
		return proxy.findSignal(tail, signalPath);
	}

	private ContactProxy produceProxyFor(Contact contact) {
		synchronized (_proxiesByContact) {
			ContactProxy proxy = _proxiesByContact.get(contact);
			if (proxy == null) {
				proxy = new ContactProxy(_injector, contact);
				_proxiesByContact.put(contact, proxy);
			}
			return proxy;
		}
	}

}
