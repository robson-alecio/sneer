package functional.adapters;

import sneer.bricks.config.SneerConfig;
import sneer.bricks.connection.SocketOriginator;
import sneer.bricks.connection.SocketReceiver;
import sneer.bricks.contacts.Contact;
import sneer.bricks.contacts.ContactManager;
import sneer.bricks.internetaddresskeeper.InternetAddressKeeper;
import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.keymanager.PublicKey;
import sneer.bricks.mesh.Me;
import sneer.bricks.mesh.Party;
import sneer.bricks.network.Network;
import sneer.lego.Inject;
import spikes.legobricks.name.OwnNameKeeper;
import spikes.legobricks.name.PortKeeper;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;
import functional.SovereignParty;

public class SneerParty extends SelfInject implements SovereignParty {
	
	private static final String MOCK_ADDRESS = "localhost";

	@Inject
	private ContactManager _contactManager;
	
	@Inject
	private PortKeeper _sneerPortKeeper;
	
	@Inject
	private OwnNameKeeper _ownNameKeeper;
	
	@Inject
	private Me _me;

	@Inject
	private InternetAddressKeeper _internetAddressKeeper;
	
	@SuppressWarnings("unused") //We need to start this brick so that it listens to others and does its thing.
	@Inject
	private SocketOriginator _originator;

	@SuppressWarnings("unused") //We need to start this brick so that it listens to others and does its thing.
	@Inject
	private SocketReceiver _receiver;

	@Inject
	private KeyManager _keyManager;
	
	
	SneerParty(String name, int port, Network network, SneerConfig config) {
		super(network, config);
		setOwnName(name);
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
		_internetAddressKeeper.add(contact, MOCK_ADDRESS, sneerParty.port());
		
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
			Thread.yield();
		}
	}

    private PublicKey publicKey() {
		return _keyManager.ownPublicKey();
	}

	@Override
    public Signal<String> navigateAndGetName(String nicknamePath) {
		String[] path = nicknamePath.split("/");
		
		Party peer = _me;
		for (String nickname : path)
			peer = waitForContact(peer, nickname);
		
		return peer.signal("Name");
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

	private int port() {
        return _sneerPortKeeper.port().currentValue();
    }

}

