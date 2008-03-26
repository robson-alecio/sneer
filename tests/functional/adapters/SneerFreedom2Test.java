package functional.adapters;

import sneer.bricks.connection.impl.mock.InMemoryNetwork;
import sneer.bricks.network.Network;
import functional.Freedom2Test;
import functional.SovereignCommunity;

public class SneerFreedom2Test extends Freedom2Test {

	private static final Network NETWORK = new InMemoryNetwork();
	@Override
	protected SovereignCommunity createNewCommunity() {
	    return new SneerCommunity(NETWORK);
	}

}
