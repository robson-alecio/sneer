package sneer.bricks.mesh.impl;

import java.util.ArrayList;

import sneer.bricks.contacts.Contact;
import sneer.bricks.contacts.ContactManager;
import sneer.bricks.mesh.Me;
import sneer.lego.Inject;
import sneer.lego.Injector;
import sneer.lego.Startable;
import spikes.legobricks.name.OwnNameKeeper;
import wheel.lang.Casts;
import wheel.reactive.Signal;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class MeImpl extends AbstractParty implements Me, Startable {

	@SuppressWarnings("unused")
	private SimpleListReceiver<Contact> _contactListReceiverToAvoidGC;

	@Inject
	private ContactManager _contactManager;

	@Inject
	private Injector _injector;

	@Inject
	private OwnNameKeeper _ownNameKeeper;

	@Override
	public void start() throws Exception {
		_contactListReceiverToAvoidGC = new SimpleListReceiver<Contact>(_contactManager.contacts()){

			@Override
			protected void elementPresent(Contact contact) {
				navigateTo(contact.nickname().currentValue());
			}

			@Override
			protected void elementAdded(Contact newContact) {
				navigateTo(newContact.nickname().currentValue());
			}

			@Override
			protected void elementToBeRemoved(Contact contactRemoved) {
				crashProxy(contactRemoved.nickname());
			}
			
		};
	}


//	@Override
//	public <T> Peer navigateTo(String nickname) throws IllegalParameter {
//		Contact contact = _contactManager.contactGiven(nickname);
//		if (contact == null) throw new IllegalParameter("Nickname not found: " + nickname);
//		
//		return producePeerFor(contact);
//	}

	@Override
	public <S> Signal<S> signal(String signalPath) {
		if (!signalPath.equals("Name"))
			throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
		
		return Casts.uncheckedGenericCast(_ownNameKeeper.name());
	}

	@Override
	<S> Signal<S> signal(String signalPath, ArrayList<String> nicknamePath) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	AbstractParty produceProxyFor(String nickname) {
		Contact contact = _contactManager.contactGiven(nickname);
		return new DirectProxy(_injector, contact);
	}


	@Override
	void crash() {
		throw new IllegalStateException();
	}

}
