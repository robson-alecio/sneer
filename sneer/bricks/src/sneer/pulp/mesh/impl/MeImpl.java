package sneer.pulp.mesh.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.kernel.container.Brick;
import sneer.kernel.container.Container;
import sneer.kernel.container.Inject;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.mesh.Me;
import sneer.pulp.mesh.Party;
import wheel.lang.Functor;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.SimpleListReceiver;
import wheel.reactive.sets.SetSignal;

class MeImpl extends AbstractParty implements Me {
	
	@Inject
	static private ContactManager _contactManager;

	@Inject
	static private Container _container;

	@SuppressWarnings("unused")
	private SimpleListReceiver<Contact> _contactListReceiverToAvoidGC;

	private final Map<Contact, SignalConnection> _signalConnectionsByContact = new HashMap<Contact, SignalConnection>();


	MeImpl() {
		registerWithKeyManager();
		registerContactReceiver();
	}

	private void registerWithKeyManager() {
		PublicKey ownPK = _keyManager.ownPublicKey();
		_keyManager.partyGiven(ownPK, new Functor<PublicKey, Party>() {
			@Override
			public Party evaluate(PublicKey pk) {
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
		SignalConnection proxy = new SignalConnection(contact);
		_signalConnectionsByContact.put(contact, proxy);
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
	PublicKey producePublicKeyFor(Contact contact) {
		return _keyManager.keyGiven(contact);
	}

	@Override
	int distanceInHops() {
		return 0;
	}

	@Override
	void subscribeTo(PublicKey targetPK, String signalPath, PublicKey intermediaryPK) {
		Contact directContact = _keyManager.contactGiven(intermediaryPK);
		
		SignalConnection signalConnection = _signalConnectionsByContact.get(directContact);
		signalConnection.subscribeTo(targetPK, signalPath);
	}

	@Override
	void subscribeToContacts(PublicKey targetPK, PublicKey intermediaryPK) {
		Contact directContact = _keyManager.contactGiven(intermediaryPK);

		SignalConnection signalConnection = _signalConnectionsByContact.get(directContact);
		signalConnection.subscribeToContacts(targetPK);
	}

	@Override
	public <B extends Brick> B brickProxyFor(Class<B> brickInterface) {
		return _container.produce(brickInterface);
	}

	@Override
	void subscribeTo(PublicKey targetPK, Class<? extends Brick> brickInterface, String signalName, PublicKey intermediaryPK) {
		Contact directContact = _keyManager.contactGiven(intermediaryPK);
		
		SignalConnection signalConnection = _signalConnectionsByContact.get(directContact);
		signalConnection.subscribeTo(targetPK, brickInterface, signalName);
	}

	@Override
	public <B extends Brick> SetSignal<B> allImmediateContactBrickCounterparts(
			Class<B> class1) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}


}
