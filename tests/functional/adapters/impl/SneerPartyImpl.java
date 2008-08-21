package functional.adapters.impl;

import java.io.File;

import sneer.kernel.container.Container;
import sneer.kernel.container.Inject;
import sneer.kernel.container.utils.io.NetworkFriendly;
import sneer.pulp.brickmanager.BrickManager;
import sneer.pulp.connection.SocketOriginator;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.deployer.BrickBundle;
import sneer.pulp.deployer.BrickFile;
import sneer.pulp.deployer.Deployer;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.mesh.Me;
import sneer.pulp.mesh.Party;
import sneer.pulp.own_Name.OwnNameKeeper;
import sneer.pulp.port.PortKeeper;
import wheel.io.serialization.DeepCopier;
import wheel.lang.Threads;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;
import functional.SovereignParty;
import functional.adapters.SneerParty;

public class SneerPartyImpl implements SneerParty {
	
	static private final String MOCK_ADDRESS = "localhost";

	@Inject
	static private Container _container;

	@Inject
	static private ContactManager _contactManager;
	
	@Inject
	static private PortKeeper _sneerPortKeeper;
	
	@Inject
	static private OwnNameKeeper _ownNameKeeper;
	
	@Inject
	static private Me _me;

	@Inject
	static private InternetAddressKeeper _internetAddressKeeper;
	
	@SuppressWarnings("unused") //We need to start this brick so that it listens to others and does its thing.
	@Inject
	static private SocketOriginator _originator;

	@SuppressWarnings("unused") //We need to start this brick so that it listens to others and does its thing.
	@Inject
	static private SocketReceiver _receiver;

	@Inject
	static private KeyManager _keyManager;
	
	@Inject
	static private Deployer _deployer;
	
	@Inject
	static private BrickManager _brickManager;


	@Override
	public void setSneerPort(int port) {
		try {
			_sneerPortKeeper.portSetter().consume(port);
		} catch (IllegalParameter e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public void bidirectionalConnectTo(SovereignParty party) {
		Contact contact = addContact(party.ownName());

		SneerParty sneerParty = (SneerParty)party;
		storePublicKey(contact, sneerParty.publicKey());
		_internetAddressKeeper.add(contact, MOCK_ADDRESS, sneerParty.sneerPort());
		
		sneerParty.giveNicknameTo(this, this.ownName());
	}

	private void storePublicKey(Contact contact, PublicKey publicKey) {
		_keyManager.addKey(contact, publicKey);
	}

	private Contact addContact(String nickname) {
		try {
			return _contactManager.addContact(nickname);
		} catch (IllegalParameter e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String ownName() {
		return _ownNameKeeper.name().currentValue();
	}

	@Override
	public void setOwnName(String newName) {
		_ownNameKeeper.nameSetter().consume(newName);
	}

    @Override
    public void giveNicknameTo(SovereignParty peer, String newNickname) {
    	PublicKey publicKey = ((SneerParty)peer).publicKey();
		Contact contact = waitForContactGiven(publicKey);

		try {
			_contactManager.changeNickname(contact, newNickname);
		} catch (IllegalParameter e) {
			throw new IllegalStateException(e);
		}
    }

	private Contact waitForContactGiven(PublicKey publicKey) {
		while (true) {
			Contact contact = _keyManager.contactGiven(publicKey);
			if (contact != null) return contact;
			Threads.sleepWithoutInterruptions(1);
		}
	}

	@Override
    public PublicKey publicKey() {
		return _keyManager.ownPublicKey();
	}

	@Override
    public Signal<String> navigateAndGetName(String nicknamePath) {
		String[] path = nicknamePath.split("/");
		
		Party peer = _me;
		for (String nickname : path)
			peer = waitForContact(peer, nickname);
		
		return peer.brickProxyFor(OwnNameKeeper.class).name();
    }

	private Party waitForContact(Party peer, String nickname) {
		while (true) {
			for (Contact candidate : peer.contacts()) {
				String candidateNick = candidate.nickname().currentValue();
				if (candidateNick.equals(nickname))
					return peer.navigateTo(candidate);
			}
			Thread.yield();
		}
	}

	@Override
	public int sneerPort() {
        return _sneerPortKeeper.port().currentValue();
    }

	@Override
	public PublicKey ownPublicKey() {
		return _keyManager.ownPublicKey();
	}


	@Override
	public void meToo(SovereignParty party, String brickName) throws Exception {
		BrickFile brick = party.brick(brickName);

		//prepare to send objet via network
		((NetworkFriendly) brick).beforeSerialize();
		BrickFile copy = DeepCopier.deepCopy(brick);
		((NetworkFriendly) brick).afterSerialize();
		
		//TODO: send copy via network
		_brickManager.install(copy);
	}

	@Override
	public BrickFile brick(String brickName) {
		return _brickManager.brick(brickName);
	}

	@Override
	public BrickBundle publishBrick(File sourceDirectory) {
		BrickBundle brickBundle = _deployer.pack(sourceDirectory);
		//brickBundle.prettyPrint();
		_brickManager.install(brickBundle);
		return brickBundle;
	}

	@Override
	public Object produce(String brickName) {
		Class<?> clazz;
		try {
			clazz = Class.forName(brickName);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		return _container.produce(clazz);
	}

	
}

