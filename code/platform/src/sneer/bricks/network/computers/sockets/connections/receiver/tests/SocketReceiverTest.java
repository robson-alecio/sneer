package sneer.bricks.network.computers.sockets.connections.receiver.tests;

import static sneer.foundation.environments.Environments.my;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.junit.Test;

import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.network.computers.sockets.accepter.SocketAccepter;
import sneer.bricks.network.computers.sockets.connections.ConnectionManager;
import sneer.bricks.network.computers.sockets.connections.receiver.SocketReceiver;
import sneer.bricks.network.computers.sockets.protocol.ProtocolTokens;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.testsupport.Bind;
import sneer.foundation.brickness.testsupport.BrickTest;

public class SocketReceiverTest extends BrickTest {

	@SuppressWarnings("unused")
	private SocketReceiver _subject;

	@Bind private final SocketAccepter _accepterMock = mock(SocketAccepter.class);
	@Bind private final ConnectionManager _connectionManagerMock = mock(ConnectionManager.class);

	private final ByteArraySocket _socket = mock(ByteArraySocket.class);
	private final EventNotifier<ByteArraySocket> _acceptedSocketNotifier = my(EventNotifiers.class).create();
	private final ContactManager _contactManager = my(ContactManager.class);

	@Test (timeout = 2000)
	public void reception() throws Exception {
		final Latch _ready = my(Threads.class).newLatch();
		
		checking(new Expectations() {{
			Sequence sequence = newSequence("main");

			oneOf(_accepterMock).lastAcceptedSocket(); will(returnValue(_acceptedSocketNotifier.output()));

			oneOf(_socket).read(); will(returnValue(ProtocolTokens.SNEER_WIRE_PROTOCOL_1)); inSequence(sequence);
			oneOf(_socket).read(); will(returnValue("Neide".getBytes("UTF-8"))); inSequence(sequence);
			oneOf(_socket).write(ProtocolTokens.OK); inSequence(sequence);

			oneOf(_connectionManagerMock).manageIncomingSocket(_contactManager.produceContact("Neide"), _socket);
				will(new CustomAction("manageIncomingSocket") { @Override public Object invoke(Invocation ignored) {
					_ready.open(); return null;
				}});
		}});

		_subject = my(SocketReceiver.class);

		_acceptedSocketNotifier.notifyReceivers(_socket);

		_ready.waitTillOpen();
	}
}