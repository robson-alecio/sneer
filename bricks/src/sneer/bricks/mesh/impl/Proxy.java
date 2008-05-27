package sneer.bricks.mesh.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sneer.bricks.contacts.Contact;
import sneer.bricks.keymanager.PublicKey;
import wheel.lang.Casts;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;
import wheel.reactive.maps.MapRegister;
import wheel.reactive.maps.MapSignal;
import wheel.reactive.maps.impl.MapRegisterImpl;

class Proxy extends AbstractParty {

	Proxy(PublicKey publicKey) {
		if (publicKey == null) throw new IllegalArgumentException("Public key cannot be null.");
		_publicKey = publicKey;
	}

	private final PublicKey _publicKey;
	private final Set<AbstractParty> _intermediaries = new HashSet<AbstractParty>();

	protected final Map<String, Register<Object>> _registersBySignalPath = new HashMap<String, Register<Object>>();
	protected final Map<String, MapRegister<Object,Object>> _mapRegistersBySignalPath = new HashMap<String, MapRegister<Object,Object>>();
	private ListRegister<RemoteContact> _contactsCache;

	@Override
	public <K,V> MapSignal<K,V> mapSignal(String signalPath) {
		MapRegister<K, V> register = produceMapRegisterFor(signalPath);
		return register.output();   //Fix: Signal type mismatch between peers is possible.
	}

	@Override
	public <S> Signal<S> signal(String signalPath) {
		Register<S> register = produceRegisterFor(signalPath);
		return register.output();   //Fix: Signal type mismatch between peers is possible. 
	}

	private <T> Register<T> produceRegisterFor(String signalPath) {
		Register<Object> register = _registersBySignalPath.get(signalPath);
		if (register == null) {
			register = new RegisterImpl<Object>(null);
			_registersBySignalPath.put(signalPath, register);
			subscribeTo(signalPath);
		}
		return Casts.uncheckedGenericCast(register);
	}

	private <K, V> MapRegister<K, V> produceMapRegisterFor(String signalPath) {
		MapRegister<Object, Object> register = _mapRegistersBySignalPath.get(signalPath);
		if (register == null) {
			register = new MapRegisterImpl<Object,Object>();
			_mapRegistersBySignalPath.put(signalPath, register);
			subscribeTo(signalPath);
		}
		return Casts.uncheckedGenericCast(register);
	}

	private void subscribeTo(String signalPath) {
		subscribeTo(_publicKey, signalPath, null);
	}

	private AbstractParty closestIntermediary() { //Optimize
		int closestDistance = Integer.MAX_VALUE;
		AbstractParty result = null;
		
		for (AbstractParty candidate : _intermediaries) {
			int distance = candidate.distanceInHops();
			if (distance == 0) return candidate;
			if (distance < closestDistance) {
				closestDistance = distance;
				result = candidate;
			}
		}

		return result;
	}

	void handleNotification(String signalPath, Object notification) {
		Register<Object> register = _registersBySignalPath.get(signalPath);
		
		if (register == null) {
			System.out.println("Register is null: " + signalPath + " - Ignoring new value notification: " + notification); //Implement: Use logger instead.
			return;
		}
		
		register.setter().consume(notification);
	}

	
	void handleNotificationOfContact(RemoteContact contact) {
		for (RemoteContact candidate : _contactsCache.output())
			if (candidate.equals(contact)) {
				candidate.nicknameSetter().consume(contact.nickname().currentValue());
				return;
			}
			
		_contactsCache.add(contact);
	}
	
	void handleNotificationOfContactRemoved(RemoteContact contact) {
		_contactsCache.remove(contact);
	}
	
	
	@Override
	public ListSignal<Contact> contacts() {
		if (_contactsCache == null) initContactsCache();
		return Casts.uncheckedGenericCast(_contactsCache.output());
	}

	private void initContactsCache() {
		_contactsCache = new ListRegisterImpl<RemoteContact>();
		subscribeToContacts(_publicKey, null);
	}


	@Override
	void addIntermediaryIfNecessary(AbstractParty intermediary) {
		_intermediaries.add(intermediary);
	}

	@Override
	PublicKey producePublicKeyFor(Contact contact) {
		return ((RemoteContact)contact).publicKey();
	}

	@Override
	int distanceInHops() {
		return closestIntermediary().distanceInHops() + 1;
	}

	@Override
	void subscribeTo(PublicKey targetPK, String signalPath, PublicKey intermediaryPKIgnored) {
		closestIntermediary().subscribeTo(targetPK , signalPath, _publicKey);
	}

	@Override
	void subscribeToContacts(PublicKey targetPK, PublicKey intermediaryPKIgnored) {
		closestIntermediary().subscribeToContacts(targetPK, _publicKey);
	}

}
