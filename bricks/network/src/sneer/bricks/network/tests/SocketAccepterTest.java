package sneer.bricks.network.tests;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Test;

import sneer.bricks.network.SocketAccepter;
import sneer.lego.Brick;
import sneer.lego.tests.BrickTestSupport;
import spikes.legobricks.name.PortKeeper;
import wheel.io.network.ObjectSocket;
import wheel.lang.Omnivore;

public class SocketAccepterTest extends BrickTestSupport {

	@Brick
	private PortKeeper _portKeeper;
	
	@Brick
	private SocketAccepter _accepter; 
	
	@Test(timeout=3000)
	public void testAccept() throws Exception {
		_portKeeper.portSetter().consume(9090);
		_accepter.lastAcceptedSocket().addReceiver(new Omnivore<ObjectSocket>() { @Override public void consume(ObjectSocket valueObject) {
			System.out.println("Socket Accepted");
		}});
		
		SneerClient client = new SneerClient();
		while(client.openConnection(9090));
		String reply = client.talk("Hello");
		System.out.println("Reply: "+reply);
		
	}
}


class SneerClient {
	
	private Socket _socket;
	
	private OutputStream _output;
	
	private BufferedInputStream _input;
	
	public boolean openConnection(int port) {
		System.err.println("open client connection to: "+port);
		try {
			_socket = new Socket("127.0.0.1", port);
			_output = _socket.getOutputStream();
			_input = new BufferedInputStream(_socket.getInputStream());
			return true;
		} catch (Exception ignored) {
			return false;
		}
	}
	
	public String talk(String message) throws IOException {
		_output.write(message.getBytes());
		byte[] buffer = new byte[1024];
		_input.read(buffer);
		return new String(buffer);
	}
}