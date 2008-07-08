package sneer.bricks.network.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import sneer.bricks.network.ByteArrayServerSocket;
import sneer.bricks.network.ByteArraySocket;
import sneer.bricks.network.Network;
import sneer.bricks.network.impl.inmemory.InMemoryNetwork;
import sneer.bricks.threadpool.ThreadPool;
import sneer.lego.Binder;
import sneer.lego.Inject;
import sneer.lego.impl.SimpleBinder;
import sneer.lego.tests.BrickTestSupport;


public class InMemoryNetworkTest extends BrickTestSupport {

	@Inject
	private Network _network;
	
	@Inject
	private ThreadPool _threadPool;

	
	@Override
	protected Binder getBinder() {
		return new SimpleBinder().bind(new InMemoryNetwork());
	}

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