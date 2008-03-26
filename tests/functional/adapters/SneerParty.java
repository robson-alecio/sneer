package functional.adapters;

import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.mesh.Mesh;
import sneer.bricks.network.Network;
import sneer.contacts.Contact;
import sneer.contacts.ContactManager;
import sneer.lego.Brick;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.impl.SimpleBinder;
import spikes.legobricks.name.OwnNameKeeper;
import wheel.lang.exceptions.IllegalParameter;
import functional.SovereignParty;

public class SneerParty implements SovereignParty {
	
	private static final String MOCK_ADDRESS = "localhost";

	@Brick
	private ContactManager _contactManager;
	
	@Brick
	private ConnectionManager _connectionManager;
	
	@Brick
	private OwnNameKeeper _ownNameKeeper;
	
	@Brick
	private Mesh _mesh;
	
	public SneerParty(String name, int port, Network network) {
		
		Container c = ContainerUtils.newContainer(new SimpleBinder().bind(Network.class).toInstance(network), null); 
		c.inject(this);

		setOwnName(name);
		try {
			_connectionManager.sneerPortSetter().consume(port);
		} catch (IllegalParameter e) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void bidirectionalConnectTo(SovereignParty party) {
		int port = ((SneerParty) party).port();
		Contact contact = _contactManager.addContact(party.ownName(), MOCK_ADDRESS, port);
		System.out.println("Contact "+contact.host() + ":" + contact.port()+" added");
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
        return _connectionManager.sneerPort().currentValue();
    }

}

