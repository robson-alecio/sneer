package sneer.pulp.network.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.BrickTestSupport;
import sneer.pulp.network.ByteArrayServerSocket;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.network.Network;
import sneer.pulp.threadpool.ThreadPool;


public class NetworkTest extends BrickTestSupport {

	@Inject
	private Network _network;
	
	@Inject
	private ThreadPool _threadPool;

	
	@Test
	public void testNetworkMessages() throws Exception {
		final ByteArrayServerSocket server = _network.openServerSocket(9090);
		_threadPool.registerActor(new Runnable(){ @Override public void run() {
			try {
				ByteArraySocket request = server.accept();
				request.write(new String(request.read()).toUpperCase().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}});
		ByteArraySocket client = _network.openSocket("localhost", 9090);
		client.write("hello".getBytes());
		byte[] reply = client.read();
		assertEquals("HELLO", new String(reply));
	}
}