package sneer.pulp.connection.tests;

import static wheel.lang.Environments.my;

import java.io.IOException;

import org.junit.Test;

import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.network.Network;
import sneer.pulp.network.inprocess.impl.InProcessNetwork;
import sneer.pulp.port.PortKeeper;
import tests.Contribute;
import tests.TestInContainerEnvironment;
import wheel.reactive.impl.Receiver;

public class SocketAccepterTest extends TestInContainerEnvironment {

	@Contribute
	private final Network _network = new InProcessNetwork();

	private final PortKeeper _portKeeper = my(PortKeeper.class);
	
	private final SocketAccepter _accepter = my(SocketAccepter.class);


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
	
	class SneerClient {
		
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


