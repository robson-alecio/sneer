package functionaltests.adapters;

import java.util.HashMap;
import java.util.Map;

import sneer.lego.ConfigurationFactory;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.impl.SimpleBinder;
import sneer.lego.impl.SimpleContainer;
import sneer.lego.tests.MockConfigurationFactory;
import sneer.network.Network;
import sneer.network.impl.tests.MockNetwork;
import functionaltests.SovereignCommunity;
import functionaltests.SovereignParty;

public class SneerSovereignCommunity implements SovereignCommunity {

    private static Network _network;
    
	@Override
	public SovereignParty createParty(String address, int port, String name) {
		SovereignParty party = new SneerSovereignParty(address, port, name);
		//Container c = ContainerUtils.newContainer(new SimpleBinder().bind(Network.class).toInstance(singleNetwork())); 
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("network.server.port", port);
		ConfigurationFactory configurationFactory = new MockConfigurationFactory(values);
		Container c = ContainerUtils.newContainer(new SimpleBinder(), configurationFactory);
		c.inject(party);
		return party;
	}

    private Network singleNetwork()
    {
        if(_network != null)
            return _network;
        
        _network = new MockNetwork();
        return _network;
    }
}
