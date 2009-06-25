package sneer.bricks.network.computers.sockets.connections.receiver.tests;

import static sneer.foundation.environments.Environments.my;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.junit.Ignore;
import org.junit.Test;

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

	@Bind private final SocketAccepter _socketAccepterMock = mock(SocketAccepter.class);
	@Bind private final ConnectionManager _connectionManagerMock = mock(ConnectionManager.class);

	private final ByteArraySocket _acceptedSocket = mock(ByteArraySocket.class);
	private final EventNotifier<ByteArraySocket> _acceptedSocketNotifier = my(EventNotifiers.class).create();
	private final ContactManager _contactManager = my(ContactManager.class);

	@Ignore
	@Test (timeout = 2000)
	public void reception() throws Exception {

		checking(new Expectations() {{
			Sequence sequence = newSequence("main");

			oneOf(_socketAccepterMock).lastAcceptedSocket();
				will(returnValue(_acceptedSocketNotifier.output()));

			oneOf(_acceptedSocket).read(); inSequence(sequence);
				will(returnValue(ProtocolTokens.SNEER_WIRE_PROTOCOL_1));

			oneOf(_acceptedSocket).read(); inSequence(sequence);
				will(returnValue("Neide".getBytes("UTF-8")));

			oneOf(_acceptedSocket).write(ProtocolTokens.OK); inSequence(sequence);

			oneOf(_connectionManagerMock).manageIncomingSocket(_contactManager.contactGiven("Neide"), _acceptedSocket);

		}});

		_subject = my(SocketReceiver.class);

		_acceptedSocketNotifier.notifyReceivers(_acceptedSocket);

		while(_contactManager.contactGiven("Neide") == null) {
			my(Threads.class).sleepWithoutInterruptions(10);
		}
	}
}