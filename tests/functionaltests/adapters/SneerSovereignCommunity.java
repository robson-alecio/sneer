package functionaltests.adapters;

import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.impl.SimpleBinder;
import sneer.network.Network;
import sneer.network.impl.tests.MockNetwork;
import functionaltests.SovereignCommunity;
import functionaltests.SovereignParty;

public class SneerSovereignCommunity implements SovereignCommunity {

    private static Network _network;
    
	@Override
	public SovereignParty createParty(String address, int port, String name) {
		SovereignParty party = new SneerSovereignParty(address, port, name);
		Container c = ContainerUtils.newContainer(new SimpleBinder().bind(Network.class).toInstance(singleNetwork())); 
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
