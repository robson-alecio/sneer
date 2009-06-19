package sneer.tests.utils.network.tests;

import sneer.bricks.pulp.network.Network;
import sneer.bricks.pulp.network.tests.NetworkTest;
import sneer.foundation.brickness.testsupport.Contribute;
import sneer.tests.utils.network.InProcessNetwork;

public class InProcessNetworkTest extends NetworkTest {

	// will automatically be made available in the container
	// by ContainerEnvironment
	@Contribute final Network _subject = new InProcessNetwork();

}
