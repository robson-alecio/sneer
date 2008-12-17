package sneer.pulp.network.tests;

import java.io.IOException;

import org.junit.Test;

import sneer.pulp.network.ByteArrayServerSocket;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.network.Network;
import sneer.pulp.threadpool.ThreadPool;
import tests.TestInContainerEnvironment;
import static wheel.lang.Environments.my;


public class NetworkTest extends TestInContainerEnvironment {

	private Network _network = my(Network.class);
	
	private ThreadPool _threadPool = my(ThreadPool.class);

	
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