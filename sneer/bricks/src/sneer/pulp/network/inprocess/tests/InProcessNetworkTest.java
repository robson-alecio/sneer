package sneer.pulp.network.inprocess.tests;

import sneer.pulp.network.Network;
import sneer.pulp.network.inprocess.impl.InProcessNetwork;
import sneer.pulp.network.tests.NetworkTest;
import tests.Contribute;

public class InProcessNetworkTest extends NetworkTest {

	// will automatically be made available in the container
	// by ContainerEnvironment
	@Contribute final Network _subject = new InProcessNetwork();

}
