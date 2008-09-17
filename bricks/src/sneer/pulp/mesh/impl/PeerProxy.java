package sneer.pulp.mesh.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sneer.kernel.container.Brick;
import sneer.kernel.container.Inject;
import sneer.pulp.contacts.Contact;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import wheel.lang.Types;
import wheel.reactive.Register;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

class PeerProxy extends AbstractParty implements SignalPublisher {

	@Inject
	static protected KeyManager _keyManager;
	
	private final PublicKey _publicKey;
	private final Set<AbstractParty> _intermediaries = new HashSet<AbstractParty>();

	private Map<Class<? extends Brick>, Brick> _brickProxiesByInterface = new HashMap<Class<? extends Brick>, Brick>();

	protected final Map<String, Register<Object>> _registersBySignalPath = new HashMap<String, Register<Object>>();
	private ListRegister<RemoteContact> _contactsCache;

	PeerProxy(PublicKey publicKey) {
		if (publicKey == null) throw new IllegalArgumentException("Public key cannot be null.");
		_publicKey = publicKey;
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

	void handleNotification(Class<? extends Brick> brickInterface, String signalName, Object notification) {
		if (brickInterface != null) {
			Brick brick = brickProxyFor(brickInterface);
			BrickProxy.handleNotification(brick, signalName, notification);
			return;
		}
		
		Register<Object> register = _registersBySignalPath.get(signalName);
		
		if (register == null) {
			System.out.println("Register is null: " + signalName + " - Ignoring new value notification: " + notification); //Implement: Use logger instead.
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
		return Types.cast(_contactsCache.output());
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

	@Override
	public synchronized <B extends Brick> B brickProxyFor(Class<B> brickInterface) {
		Brick result = _brickProxiesByInterface.get(brickInterface);
		if (result == null) {
			result = BrickProxy.createFor(brickInterface, this);
			
			_brickProxiesByInterface.put(brickInterface, result);
		}
		return (B)result;
	}

	@Override
	public void subscribeTo(Class<? extends Brick> brickInterface, String signalName) {
		subscribeTo(_publicKey, brickInterface, signalName, null);
	}

	@Override
	void subscribeTo(PublicKey targetPK, Class<? extends Brick> brickInterface, String signalName, PublicKey intermediaryPKIgnored) {
		closestIntermediary().subscribeTo(targetPK , brickInterface, signalName, _publicKey);
	}

	@Override
	protected KeyManager keyManager() {
		return _keyManager;
	}


}
