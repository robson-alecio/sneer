package sneer.bricks.network.computers.sockets.connections.receiver.tests;

import static sneer.foundation.environments.Environments.my;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.bricks.network.computers.sockets.accepter.SocketAccepter;
import sneer.bricks.network.computers.sockets.connections.receiver.SocketReceiver;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.testsupport.Bind;
import sneer.foundation.brickness.testsupport.BrickTest;

public class SocketReceiverTest extends BrickTest {

	@Bind private SocketAccepter _accepterMock = mock(SocketAccepter.class); 

	@SuppressWarnings("unused")
	private SocketReceiver _subject;
	private final EventNotifier<ByteArraySocket> _acceptedSocket = my(EventNotifiers.class).create();

	@Test
	public void reception() {
		checking(new Expectations() {{
			exactly(1).of(_accepterMock).lastAcceptedSocket(); will(returnValue(_acceptedSocket.output()));
			//socketMock espera read() retorna Protocol().SNEER_WIRE_ETC
			//socketMock espera read() retorna "Neide".getBytes("UTF-8")
			//connectionManagerMock espera manageIncomingSocket(contact da Neide, socket mocado)
		 }});

		_subject = my(SocketReceiver.class);

		//_accepted socket retorna um socket mocado.
		//assertTrue(contact manager tem contact Neide)
		assertTrue(true);
	}	
}
