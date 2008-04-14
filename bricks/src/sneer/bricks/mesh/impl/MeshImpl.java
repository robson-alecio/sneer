package sneer.bricks.mesh.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.contacts.Contact;
import sneer.bricks.contacts.ContactManager;
import sneer.bricks.mesh.Mesh;
import sneer.bricks.mesh.Peer;
import sneer.lego.Inject;
import sneer.lego.Injector;
import sneer.lego.Startable;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class MeshImpl implements Mesh, Startable {

	private final Map<Contact, PeerImpl> _proxiesByContact = new HashMap<Contact, PeerImpl>();
	
	@SuppressWarnings("unused")
	private SimpleListReceiver<Contact> _contactListReceiverToAvoidGC;

	@Inject
	private ContactManager _contactManager;

	@Inject
	private Injector _injector;

	@Override
	public void start() throws Exception {
		_contactListReceiverToAvoidGC = new SimpleListReceiver<Contact>(_contactManager.contacts()){

			@Override
			protected void elementPresent(Contact contact) {
				producePeerFor(contact);
			}

			@Override
			protected void elementAdded(Contact newContact) {
				producePeerFor(newContact);
			}

			@Override
			protected void elementToBeRemoved(Contact contactRemoved) {
				crashPeerFor(contactRemoved);
			}
			
		};
	}

	private void crashPeerFor(Contact contactRemoved) {
		PeerImpl peer = _proxiesByContact.remove(contactRemoved);
		peer.crash();
	}

	@Override
	public <T> Peer navigateTo(String nicknamePath) {
		String[] path = nicknamePath.split("/", 1);
		String head = path[0];
		String tail = path.length > 1
			? path[1]
			: "";
			
		Contact contact = _contactManager.contactGiven(head);
		return producePeerFor(contact);
	}

	private Peer producePeerFor(Contact contact) {
		synchronized (_proxiesByContact) {
			PeerImpl proxy = _proxiesByContact.get(contact);
			if (proxy == null) {
				proxy = new PeerImpl(_injector, contact);
				_proxiesByContact.put(contact, proxy);
			}
			return proxy;
		}
	}
}
