package functional.adapters;

import sneer.bricks.connection.SocketOriginator;
import sneer.bricks.contacts.Contact;
import sneer.bricks.contacts.ContactManager;
import sneer.bricks.internetaddresskeeper.InternetAddressKeeper;
import sneer.bricks.mesh.Mesh;
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
	private Mesh _mesh;

	@Inject
	private InternetAddressKeeper _internetAddressKeeper;
	
	@Inject
	private SocketOriginator _originator; //need to start this component so that is registers itself on InternetAddressKeeper.addresses
	
	public SneerParty(String name, int port, Network network) {
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
		Contact contact;
		try {
			contact = _contactManager.addContact(party.ownName());
		} catch (IllegalParameter e) {
			throw new IllegalStateException(e);
		}
		int port = ((SneerParty) party).port();
		_internetAddressKeeper.add(contact, MOCK_ADDRESS, port);
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
    public void giveNicknameTo(SovereignParty peer, String nickname)
    {
//        _contactManager.alias(peer, nickname);
    }

    @Override
    public Signal<String> navigateAndGetName(String nicknamePath) {
		return _mesh.navigateTo(nicknamePath).signal("Name");
    }


	public String address()
    {
        return MOCK_ADDRESS;
    }

    public int port()
    {
        return _sneerPortKeeper.port().currentValue();
    }

}

