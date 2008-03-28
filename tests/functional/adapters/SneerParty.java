package functional.adapters;

import sneer.bricks.mesh.Mesh;
import sneer.bricks.network.Network;
import sneer.contacts.Contact;
import sneer.contacts.ContactManager;
import sneer.internetaddresskeeper.InternetAddressKeeper;
import sneer.lego.Brick;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.impl.SimpleBinder;
import spikes.legobricks.name.OwnNameKeeper;
import spikes.legobricks.name.PortKeeper;
import wheel.lang.exceptions.IllegalParameter;
import functional.SovereignParty;

public class SneerParty implements SovereignParty {
	
	private static final String MOCK_ADDRESS = "localhost";

	@Brick
	private ContactManager _contactManager;
	
	@Brick
	private PortKeeper _sneerPortKeeper;
	
	@Brick
	private OwnNameKeeper _ownNameKeeper;
	
	@Brick
	private Mesh _mesh;

	@Brick
	private InternetAddressKeeper _internetAddressKeeper;
	
	public SneerParty(String name, int port, Network network) {
		
		Container c = ContainerUtils.newContainer(new SimpleBinder().bind(Network.class).toInstance(network), null); 
		c.inject(this);

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
    public String navigateAndGetName(String nicknamePath) {
    	return _mesh.findSignal(nicknamePath, "Name").currentValue();
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

