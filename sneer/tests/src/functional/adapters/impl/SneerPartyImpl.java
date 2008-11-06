package functional.adapters.impl;

import java.io.File;

import snapps.wind.Shout;
import snapps.wind.Wind;
import sneer.kernel.container.Brick;
import sneer.kernel.container.Container;
import sneer.kernel.container.Inject;
import sneer.pulp.brickmanager.BrickManager;
import sneer.pulp.clock.Clock;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.connection.SocketOriginator;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.deployer.BrickBundle;
import sneer.pulp.deployer.Deployer;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.port.PortKeeper;
import sneer.pulp.probe.ProbeManager;
import wheel.lang.Threads;
import wheel.lang.Types;
import wheel.lang.exceptions.IllegalParameter;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import functional.SovereignParty;
import functional.adapters.SneerParty;

public class SneerPartyImpl implements SneerParty {
	
	static private final String MOCK_ADDRESS = "localhost";

	@Inject
	static private Container _container;
	
	@Inject
	static private Clock _clock;

	@Inject
	static private ContactManager _contactManager;

	@Inject
	static private ConnectionManager _connectionManager;
	
	@Inject
	static private PortKeeper _sneerPortKeeper;
	
	@Inject
	static private OwnNameKeeper _ownNameKeeper;
	
	@Inject
	static private InternetAddressKeeper _internetAddressKeeper;

	@Inject
	static private Wind _wind;

	@SuppressWarnings("unused")
	@Inject
	static private ProbeManager _probes;
	
	
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
		System.out.println("Waiting");
		waitUntilOnline(contact);
		System.out.println("OK");
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
			_contactManager.nicknameSetterFor(contact).consume(newNickname);
		} catch (IllegalParameter e) {
			throw new IllegalStateException(e);
		}
		
		System.out.println("Waiting");
		waitUntilOnline(contact);
		System.out.println("OK");

    }

	private void waitUntilOnline(Contact contact) {
		ByteConnection connection = _connectionManager.connectionFor(contact);
		while (!connection.isOnline().currentValue())
			Threads.sleepWithoutInterruptions(1);
	}

	private Contact waitForContactGiven(PublicKey publicKey) {
		while (true) {
			Contact contact = _keyManager.contactGiven(publicKey);
			if (contact != null) return contact;
			Threads.sleepWithoutInterruptions(1);
			_clock.advanceTime(60 * 1000);
		}
	}

	@Override
    public PublicKey publicKey() {
		return _keyManager.ownPublicKey();
	}

	@Override
    public Signal<String> navigateAndGetName(String nicknamePath) {
		throw new NotImplementedYet();
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
	public BrickBundle publishBricks(File sourceDirectory) {
		BrickBundle brickBundle = _deployer.pack(sourceDirectory);
		//brickBundle.prettyPrint();
		_brickManager.install(brickBundle);
		return brickBundle;
	}

	@Override
	public Brick produce(Class<? extends Brick> brick) {
		return Types.cast(_container.produce(brick));
	}

	@Override
	public void shout(String phrase) {
		_wind.megaphone().consume(phrase);
	}

	@Override
	public ListSignal<Shout> shoutsHeard() {
		return _wind.shoutsHeard();
	}

	
}

