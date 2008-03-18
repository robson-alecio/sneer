package functionaltests.adapters;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.mesh.Mesh;
import sneer.contacts.ConnectionManager;
import sneer.lego.Brick;
import sneer.lego.ConfigurationFactory;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.impl.SimpleBinder;
import sneer.lego.tests.MockConfigurationFactory;
import spikes.legobricks.name.OwnNameKeeper;
import functionaltests.SovereignParty;

public class SneerSovereignParty implements SovereignParty {
	
	private static final String MOCK_ADDRESS = "localhost";

//	@Brick
//	private Network _network;

	@Brick
	private ConnectionManager _signalManager;
	
	@Brick
	private OwnNameKeeper _ownNameKeeper;
	
	private int _port;

	@Brick
	private Mesh _mesh;
	
	public SneerSovereignParty(String name, int port) {
		_port = port;
		
		//Container c = ContainerUtils.newContainer(new SimpleBinder().bind(Network.class).toInstance(singleNetwork())); 
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("network.server.port", port);
		ConfigurationFactory configurationFactory = new MockConfigurationFactory(values);
		Container c = ContainerUtils.newContainer(new SimpleBinder(), configurationFactory);
		c.inject(this);

		setOwnName(name);
	}

	@Override
	public void bidirectionalConnectTo(SovereignParty peer) {
		int port = ((SneerSovereignParty)peer).port();
//	    if(_contactManager.add(peer.ownName(), MOCK_ADDRESS, port)) {
//	        //System.out.println(_name+" <-> "+peer.ownName() +" "+_contactManager);
//	    }
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
        return _port;
    }

}

