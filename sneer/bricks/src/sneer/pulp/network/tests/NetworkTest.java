package sneer.pulp.network.tests;

import static sneer.commons.environments.Environments.my;

import java.io.IOException;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.pulp.network.ByteArrayServerSocket;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.network.Network;
import sneer.pulp.threads.Stepper;
import sneer.pulp.threads.Threads;


public class NetworkTest extends BrickTest {
	
	private final Threads _threads = my(Threads.class);
	
	@Test
	public void testNetworkMessages() throws Exception {
		
		final Network network = my(Network.class);
		
		final ByteArrayServerSocket server = network.openServerSocket(9090);

		final Stepper _refToAvoidGc = new Stepper() { @Override public boolean step() {
			try {
				ByteArraySocket request = server.accept();
				request.write(new String(request.read()).toUpperCase().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}

			return false;
		}};

		_threads.registerStepper(_refToAvoidGc);

		ByteArraySocket client = network.openSocket("localhost", 9090);
		client.write("hello".getBytes());
		byte[] reply = client.read();
		assertEquals("HELLO", new String(reply));
	}
}