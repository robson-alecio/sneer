package functional.adapters;

import sneer.bricks.connection.SocketOriginator;
import sneer.bricks.connection.SocketReceiver;
import sneer.bricks.contacts.Contact;
import sneer.bricks.contacts.ContactManager;
import sneer.bricks.internetaddresskeeper.InternetAddressKeeper;
import sneer.bricks.keymanager.ContactAlreadyHadAKey;
import sneer.bricks.keymanager.KeyBelongsToOtherContact;
import sneer.bricks.keymanager.KeyManager;
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
	
	@Inject
	private SocketOriginator _originator; //need to start this component so that is registers itself on InternetAddressKeeper.addresses
	@Inject
	private SocketReceiver _receiver; //need to start this component so that is registers itself on InternetAddressKeeper.addresses

	@Inject
	private KeyManager _keyManager;
	
	
	SneerParty(String name, int port, Network network) {
		super(network);
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

	private void storePublicKey(Contact contact, byte[] publicKey) {
		try {
			_keyManager.addKey(contact, publicKey);
		} catch (ContactAlreadyHadAKey e) {
			throw new IllegalStateException(e);
		} catch (KeyBelongsToOtherContact e) {
			throw new IllegalStateException(e);
		}
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
    	byte[] publicKey = ((SneerParty)peer).publicKey();
		Contact contact = waitForContactGiven(publicKey);

		try {
			_contactManager.changeNickname(contact, newNickname);
		} catch (IllegalParameter e) {
			throw new IllegalStateException(e);
		}
    }

	private Contact waitForContactGiven(byte[] publicKey) {
		while (true) {
			Contact contact = _keyManager.contactGiven(publicKey);
			if (contact != null) return contact;
			Thread.yield();
		}
	}

    private byte[] publicKey() {
		return _keyManager.ownPublicKey();
	}

	@Override
    public Signal<String> navigateAndGetName(String nicknamePath) {
		String[] path = nicknamePath.split("/");
		
		Party peer = _me;
		for (String nickname : path)
			peer = navigate(peer, nickname);
		
		return peer.signal("Name");
    }

	private Party navigate(Party peer, String nickname) {
		try {
			return peer.navigateTo(nickname);
		} catch (IllegalParameter e) {
			throw new RuntimeException(e);
		}
	}


    private int port() {
        return _sneerPortKeeper.port().currentValue();
    }

}

