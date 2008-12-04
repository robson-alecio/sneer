package sneer.pulp.network.impl.inmemory.tests;

import sneer.pulp.network.impl.inmemory.InMemoryNetwork;
import sneer.pulp.network.tests.NetworkTest;

public class InMemoryNetworkTest extends NetworkTest {

	// will automatically be made available in the container
	// by ContainerEnvironment
	InMemoryNetwork _subject = new InMemoryNetwork();

}
