package sneer.pulp.connection.tests;

import java.io.IOException;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.network.Network;
import sneer.pulp.network.impl.inmemory.InMemoryNetwork;
import sneer.pulp.port.PortKeeper;
import wheel.reactive.impl.Receiver;

public class SocketAccepterTest extends TestThatIsInjected {

	@Inject
	private static PortKeeper _portKeeper;
	
	@Inject
	private static SocketAccepter _accepter;

	@Inject
	private static Network _network;
	
	
	
	@Override
	protected Object[] getBindings() {
		return new Object[]{ new InMemoryNetwork() };
	}

	@Test(timeout=5000)
	public void testSocketAccept() throws Exception {
		_portKeeper.portSetter().consume(9090);

		@SuppressWarnings("unused")
		Receiver<ByteArraySocket> _acceptedSocketReceiverAvoidGc = new Receiver<ByteArraySocket>(_accepter.lastAcceptedSocket()) { @Override public void consume(ByteArraySocket socket) {
			try {
				byte[] request = socket.read();
				String s = new String(request);
				socket.write(s.toUpperCase().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}};

		SneerClient client = new SneerClient();
		while(!client.connected(9090));
		String reply = client.talk("hello");
		assertEquals("HELLO", reply);
	}
	
	static class SneerClient {
		
		private ByteArraySocket _socket;
		
		boolean connected(int port) {
			try {
				_socket = _network.openSocket("localhost", port);
				return true;
			} catch (Exception ignored) {
				//ignored.printStackTrace();
				return false;
			}
		}
		
		String talk(String message) throws IOException {
			_socket.write(message.getBytes());
			byte[] buffer = _socket.read();
			return new String(buffer);
		}
	}
}


