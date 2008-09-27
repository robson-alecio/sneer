package sneer.pulp.network.impl.inmemory.tests;

import sneer.pulp.network.impl.inmemory.InMemoryNetwork;
import sneer.pulp.network.tests.NetworkTest;

public class InMemoryNetworkTest extends NetworkTest {

	@Override
	protected Object[] getBindings() {
		return new Object[]{new InMemoryNetwork()};
	}

}
