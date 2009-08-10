package sneer.bricks.network.computers.sockets.connections.originator.tests;

import static sneer.foundation.environments.Environments.my;

import org.jmock.Expectations;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.junit.Test;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.network.computers.sockets.connections.ByteConnection;
import sneer.bricks.network.computers.sockets.connections.ConnectionManager;
import sneer.bricks.network.computers.sockets.connections.originator.SocketOriginator;
import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.bricks.pulp.network.Network;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.brickness.testsupport.Bind;
import sneer.foundation.brickness.testsupport.BrickTest;

public class SocketOriginatorTest extends BrickTest {

	@SuppressWarnings("unused")
	private SocketOriginator _subject;

	@Bind private final Network _networkMock = mock(Network.class);
	@Bind private final ConnectionManager _connectionManagerMock = mock(ConnectionManager.class);

	private final ByteConnection _byteConnection = mock(ByteConnection.class);
	private final Signal<Boolean> _isConnected = my(Signals.class).constant(false);
	private final ByteArraySocket _openedSocket = mock(ByteArraySocket.class);


	@Test (timeout = 2000)
	public void openConnection() throws Exception {
		final Latch _ready = my(Threads.class).newLatch();
		final Contact neide = my(ContactManager.class).produceContact("Neide");

		checking(new Expectations() {{
			oneOf(_connectionManagerMock).connectionFor(neide);
				will(returnValue(_byteConnection));

			oneOf(_byteConnection).isConnected();
				will(returnValue(_isConnected));

			oneOf(_networkMock).openSocket("neide.selfip.net", 5000);
				will(returnValue(_openedSocket));

			oneOf(_connectionManagerMock).manageOutgoingSocket(_openedSocket, neide);
				will(new CustomAction("manageIncomingSocket") { @Override public Object invoke(Invocation ignored) {
					_ready.open(); return null;
				}});
		}});

		_subject = my(SocketOriginator.class);

		my(InternetAddressKeeper.class).add(neide, "neide.selfip.net", 5000);
		my(Clock.class).advanceTime(1);
		_ready.waitTillOpen();
	}
}
