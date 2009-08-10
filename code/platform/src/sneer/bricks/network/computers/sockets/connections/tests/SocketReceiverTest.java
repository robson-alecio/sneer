package sneer.bricks.network.computers.sockets.connections.tests;

import static sneer.foundation.environments.Environments.my;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.junit.Ignore;
import org.junit.Test;

import sneer.bricks.network.computers.sockets.connections.ConnectionManager;
import sneer.bricks.network.computers.sockets.protocol.ProtocolTokens;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.testsupport.BrickTest;

@Ignore
public class SocketReceiverTest extends BrickTest {

	private ConnectionManager _subject = my(ConnectionManager.class);

	private final ByteArraySocket _socket1 = mock(ByteArraySocket.class, "socket1");
	private final ByteArraySocket _socket2 = mock(ByteArraySocket.class, "socket2");

	@Test (timeout = 2000)
	public void reception() throws Exception {
		
		checking(new Expectations() {{
			Sequence sequence = newSequence("main");

			oneOf(_socket1).read(); will(returnValue(ProtocolTokens.SNEER_WIRE_PROTOCOL_1)); inSequence(sequence);
			oneOf(_socket1).read(); will(returnValue("Neide".getBytes("UTF-8"))); inSequence(sequence);
			oneOf(_socket1).write(ProtocolTokens.OK); inSequence(sequence);

			oneOf(_socket2).read(); will(returnValue(ProtocolTokens.SNEER_WIRE_PROTOCOL_1)); inSequence(sequence);
			oneOf(_socket2).read(); will(returnValue("Neide".getBytes("UTF-8"))); inSequence(sequence);
			oneOf(_socket2).write(ProtocolTokens.OK); inSequence(sequence);
			oneOf(_socket2).close(); inSequence(sequence);
			
		}});

		_subject.manageIncomingSocket(_socket1);
		_subject.manageIncomingSocket(_socket2);
	}
}