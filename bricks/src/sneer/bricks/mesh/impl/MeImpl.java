package sneer.bricks.mesh.impl;

import java.util.HashMap;
import java.util.Map;

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

	@Inject
	private ContactManager _contactManager;

	@Inject
	private Injector _injector;

	@Inject
	private OwnNameKeeper _ownNameKeeper;

	@SuppressWarnings("unused")
	private SimpleListReceiver<Contact> _contactListReceiverToAvoidGC;

	private final Map<Contact, DirectProxy> _directProxiesByContact = new HashMap<Contact, DirectProxy>();
	
	@Override
	public void start() throws Exception {
		_contactListReceiverToAvoidGC = new SimpleListReceiver<Contact>(_contactManager.contacts()){

			@Override
			protected void elementPresent(Contact contact) {
				createDirectProxyFor(contact);
			}

			@Override
			protected void elementAdded(Contact contact) {
				createDirectProxyFor(contact);
			}

			@Override
			protected void elementToBeRemoved(Contact contact) {
				_directProxiesByContact.remove(contact).crash();
			}
			
		};
	}

	private void createDirectProxyFor(Contact contact) {
		DirectProxy proxy = new DirectProxy(_injector, contact);
		_directProxiesByContact.put(contact, proxy);
	}


	@Override
	public <S> Signal<S> signal(String signalPath) {
		if (!signalPath.equals("Name"))
			throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
		
		return Casts.uncheckedGenericCast(_ownNameKeeper.name());
	}


	@Override
	AbstractParty produceProxyFor(String nickname) {
		Contact contact = _contactManager.contactGiven(nickname);
		return _directProxiesByContact.get(contact);
	}


}
