package sneer.bricks.mesh.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.contacts.Contact;
import sneer.bricks.contacts.ContactManager;
import sneer.bricks.crypto.Sneer1024;
import sneer.bricks.mesh.Me;
import sneer.bricks.mesh.Party;
import sneer.lego.Inject;
import sneer.lego.Injector;
import sneer.lego.Startable;
import spikes.legobricks.name.OwnNameKeeper;
import wheel.lang.Casts;
import wheel.lang.Functor;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.SimpleListReceiver;


class MeImpl extends AbstractParty implements Me, Startable {

	@Inject
	private ContactManager _contactManager;

	@Inject
	private Injector _injector;

	@Inject
	private OwnNameKeeper _ownNameKeeper;

	@SuppressWarnings("unused")
	private SimpleListReceiver<Contact> _contactListReceiverToAvoidGC;

	private final Map<Contact, SignalConnection> _signalConnectionsByContact = new HashMap<Contact, SignalConnection>();

	
	@Override
	public void start() throws Exception {
		registerWithKeyManager();
		registerContactReceiver();
	}

	private void registerWithKeyManager() {
		Sneer1024 ownPK = _keyManager.ownPublicKey();
		_keyManager.partyGiven(ownPK, new Functor<Sneer1024, Party>() {
			@Override
			public Party evaluate(Sneer1024 pk) {
				return MeImpl.this;
			}
		});
	}

	private void registerContactReceiver() {
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
				_signalConnectionsByContact.remove(contact).crash();
			}
			
		};
	}

	private void createDirectProxyFor(Contact contact) {
		SignalConnection proxy = new SignalConnection(_injector, contact);
		_signalConnectionsByContact.put(contact, proxy);
	}


	@Override
	public <S> Signal<S> signal(String signalPath) {
		if (!signalPath.equals("Name"))
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	
		return Casts.uncheckedGenericCast(_ownNameKeeper.name());
	}

	@Override
	public ListSignal<Contact> contacts() {
		return _contactManager.contacts();
	}

	@Override
	void addIntermediaryIfNecessary(AbstractParty ignored) {
		//I dont need intermediaries to communicate with myself.
	}

	@Override
	Sneer1024 producePublicKeyFor(Contact contact) {
		return _keyManager.keyGiven(contact);
	}

	@Override
	int distanceInHops() {
		return 0;
	}

	@Override
	void subscribeTo(Sneer1024 targetPK, String signalPath, Sneer1024 intermediaryPK) {
		Contact directContact = _keyManager.contactGiven(intermediaryPK);
		
		SignalConnection signalConnection = _signalConnectionsByContact.get(directContact);
		signalConnection.subscribeTo(targetPK, signalPath);
	}



}
