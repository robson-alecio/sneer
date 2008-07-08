package functional.adapters.impl;

import java.io.File;

import sneer.bricks.brickmanager.BrickManager;
import sneer.bricks.connection.SocketOriginator;
import sneer.bricks.connection.SocketReceiver;
import sneer.bricks.contacts.Contact;
import sneer.bricks.contacts.ContactManager;
import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.BrickFile;
import sneer.bricks.deployer.Deployer;
import sneer.bricks.internetaddresskeeper.InternetAddressKeeper;
import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.keymanager.PublicKey;
import sneer.bricks.mesh.Me;
import sneer.bricks.mesh.Party;
import sneer.bricks.ownName.OwnNameKeeper;
import sneer.bricks.port.PortKeeper;
import sneer.lego.Container;
import sneer.lego.Inject;
import sneer.lego.utils.io.NetworkFriendly;
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
		return _container.produce(brickName);
	}

	
}

