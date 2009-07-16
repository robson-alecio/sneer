package sneer.bricks.network.computers.sockets.connections.originator.tests;

import static sneer.foundation.environments.Environments.my;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.junit.Test;

import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.network.computers.sockets.connections.ConnectionManager;
import sneer.bricks.network.computers.sockets.connections.originator.SocketOriginator;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.bricks.pulp.network.Network;
import sneer.foundation.brickness.testsupport.Bind;
import sneer.foundation.brickness.testsupport.BrickTest;

public class SocketOriginatorTest extends BrickTest {

	@SuppressWarnings("unused")
	private SocketOriginator _subject;

	@Bind private final Network _networkMock = mock(Network.class);
	@Bind private final ConnectionManager _connectionManagerMock = mock(ConnectionManager.class);

	private final ByteArraySocket _openedSocket = mock(ByteArraySocket.class);
	private final InternetAddressKeeper _internetAddressKeeper = my(InternetAddressKeeper.class);
	private final ContactManager _contactManager = my(ContactManager.class);

	@Test (timeout = 2000)
	public void openConnection() throws Exception {
		final Latch _ready = my(Threads.class).newLatch();

		checking(new Expectations() {{
			Sequence sequence = newSequence("main");

			oneOf(_networkMock).openSocket("neide.selfip.net", 5000);
				will(returnValue(_openedSocket));

			oneOf(_connectionManagerMock).manageOutgoingSocket(_contactManager.produceContact("Neide"), _openedSocket); inSequence(sequence);
				will(new CustomAction("manageIncomingSocket") { @Override public Object invoke(Invocation ignored) {
					_ready.open(); return null;
				}});
		}});

		_subject = my(SocketOriginator.class);

		_internetAddressKeeper.add(_contactManager.produceContact("Neide"), "neide.selfip.net", 5000);

		_ready.await();
	}
}
