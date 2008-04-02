package sneer.bricks.connection.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.Socket;

import org.junit.Test;

import sneer.bricks.connection.SocketAccepter;
import sneer.bricks.network.ByteArraySocket;
import sneer.bricks.network.impl.ByteArraySocketImpl;
import sneer.lego.Brick;
import sneer.lego.tests.BrickTestSupport;
import spikes.legobricks.name.PortKeeper;
import wheel.lang.Omnivore;

public class SocketAccepterTest extends BrickTestSupport {

	@Brick
	private PortKeeper _portKeeper;
	
	@Brick
	private SocketAccepter _accepter;
	
	@Test(timeout=3000)
	public void testSocketAccept() throws Exception {
		_portKeeper.portSetter().consume(9090);
		Omnivore<ByteArraySocket> omnivore = new Omnivore<ByteArraySocket>() { @Override public void consume(ByteArraySocket socket) {
			try {
				byte[] request = socket.read();
				String s = new String(request);
				socket.write(s.toUpperCase().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}};
		_accepter.lastAcceptedSocket().addReceiver(omnivore);

		SneerClient client = new SneerClient();
		while(!client.connected(9090));
		String reply = client.talk("hello");
		assertEquals("HELLO", reply);
	}
}


class SneerClient {
	
	private ByteArraySocket _socket;
	
	public boolean connected(int port) {
		try {
			_socket = new ByteArraySocketImpl(new Socket("127.0.0.1", port));
			return true;
		} catch (Exception ignored) {
			//ignored.printStackTrace(System.err);
			return false;
		}
	}
	
	public String talk(String message) throws IOException {
		_socket.write(message.getBytes());
		byte[] buffer = _socket.read();
		return new String(buffer);
	}
}