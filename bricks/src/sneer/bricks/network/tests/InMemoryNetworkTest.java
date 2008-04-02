package sneer.bricks.network.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import sneer.bricks.network.ByteArrayServerSocket;
import sneer.bricks.network.ByteArraySocket;
import sneer.bricks.network.Network;
import sneer.bricks.network.impl.inmemory.InMemoryNetwork;
import sneer.lego.Binder;
import sneer.lego.Brick;
import sneer.lego.impl.SimpleBinder;
import sneer.lego.tests.BrickTestSupport;
import wheel.lang.Threads;


public class InMemoryNetworkTest extends BrickTestSupport {

	@Brick
	private Network _network;
	
	@Override
	protected Binder getBinder() {
		return new SimpleBinder().bind(Network.class).toInstance(new InMemoryNetwork());
	}

	@Test
	public void testNetworkMessages() throws Exception {
		final ByteArrayServerSocket server = _network.openServerSocket(9090);
		Threads.startDaemon(new Runnable(){ @Override public void run() {
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