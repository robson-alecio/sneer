package sneer.bricks.mesh.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sneer.bricks.contacts.Contact;
import sneer.bricks.crypto.Sneer1024;
import wheel.lang.Casts;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

class Proxy extends AbstractParty {

	Proxy(Sneer1024 publicKey) {
		_publicKey = publicKey;
	}

	
	private final Sneer1024 _publicKey;
	private final Set<AbstractParty> _intermediaries = new HashSet<AbstractParty>();

	protected final Map<String, Register<Object>> _registersBySignalPath = new HashMap<String, Register<Object>>();
	private ListSource<Contact> _contactsCache;

	
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
		if ("Contacts".equals(signalPath))
			throw new NotImplementedYet();
		
		_registersBySignalPath.get(signalPath).setter().consume(notification);
	}

	
	@Override
	public ListSignal<Contact> contacts() {
		if (_contactsCache == null) initContactsCache();
		return _contactsCache.output();
	}

	private void initContactsCache() {
		_contactsCache = new ListSourceImpl<Contact>();
		subscribeTo("Contacts");
	}

	@Override
	void addIntermediaryIfNecessary(AbstractParty intermediary) {
		_intermediaries.add(intermediary);
	}

	@Override
	Sneer1024 producePublicKeyFor(Contact contact) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	int distanceInHops() {
		return closestIntermediary().distanceInHops() + 1;
	}

	@Override
	void subscribeTo(Sneer1024 targetPK, String signalPath, Sneer1024 intermediaryPKIgnored) {
		closestIntermediary().subscribeTo(targetPK , signalPath, _publicKey);
	}


}
