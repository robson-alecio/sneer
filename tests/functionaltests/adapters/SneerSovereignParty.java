package functionaltests.adapters;

import java.util.HashMap;
import java.util.Map;

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
//
//	@Brick
//	private ContactManager _contactManager;
	
	@Brick
	private OwnNameKeeper _ownNameKeeper;
	
	private int _port;
	
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
		return _ownNameKeeper.getName();
	}

	@Override
	public void setOwnName(String newName) {
		_ownNameKeeper.setName(newName);
	}

    @Override
    public void giveNicknameTo(SovereignParty peer, String nickname)
    {
//        _contactManager.alias(peer, nickname);
    }

    @Override
    public SovereignParty navigateTo(String... nicknamePath)
    {
        throw new wheel.lang.exceptions.NotImplementedYet();
    }

    public String address()
    {
        return MOCK_ADDRESS;
    }

    public int port()
    {
        return _port;
    }

    @Override
    public String toString() {
        return "SneerSovereignParty:" + ownName();
    }

}

