package sneer.pulp.network.impl.inmemory.tests;

import sneer.pulp.network.impl.inmemory.InMemoryNetwork;
import sneer.pulp.network.tests.NetworkTest;
import tests.Contribute;

public class InMemoryNetworkTest extends NetworkTest {

	// will automatically be made available in the container
	// by ContainerEnvironment
	@Contribute final InMemoryNetwork _subject = new InMemoryNetwork();

}
