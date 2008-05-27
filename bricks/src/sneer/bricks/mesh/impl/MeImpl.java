package sneer.bricks.mesh.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.brickmanager.BrickManager;
import sneer.bricks.contacts.Contact;
import sneer.bricks.contacts.ContactManager;
import sneer.bricks.keymanager.PublicKey;
import sneer.bricks.mesh.Me;
import sneer.bricks.mesh.Party;
import sneer.lego.Inject;
import sneer.lego.Startable;
import spikes.legobricks.name.OwnNameKeeper;
import wheel.lang.Casts;
import wheel.lang.Functor;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.SimpleListReceiver;
import wheel.reactive.maps.MapSignal;


class MeImpl extends AbstractParty implements Me, Startable {

	@Inject
	static private ContactManager _contactManager;

	@Inject
	static private OwnNameKeeper _ownNameKeeper;

	@Inject
	static private BrickManager _brickManager;

	@SuppressWarnings("unused")
	private SimpleListReceiver<Contact> _contactListReceiverToAvoidGC;

	private final Map<Contact, SignalConnection> _signalConnectionsByContact = new HashMap<Contact, SignalConnection>();


	
	@Override
	public void start() throws Exception {
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
	public <K,V> MapSignal<K,V> mapSignal(String signalPath) {
		if (signalPath.equals("Bricks"))
			return Casts.uncheckedGenericCast(_brickManager.bricks());

		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public <S> Signal<S> signal(String signalPath) {
		if (signalPath.equals("Name"))
			return Casts.uncheckedGenericCast(_ownNameKeeper.name());
			
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
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


}
