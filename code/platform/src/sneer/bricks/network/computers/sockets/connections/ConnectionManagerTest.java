package sneer.bricks.network.computers.sockets.connections;

import static sneer.foundation.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.network.computers.sockets.connections.originator.SocketOriginator;
import sneer.bricks.network.computers.sockets.connections.receiver.SocketReceiver;
import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.foundation.brickness.testsupport.BrickTest;

public class ConnectionManagerTest extends BrickTest {

	private final ConnectionManager _subject = my(ConnectionManager.class);

	private final ContactManager _contactManager = my(ContactManager.class);
	private final InternetAddressKeeper _addressKeeper = my(InternetAddressKeeper.class);

	@SuppressWarnings("unused") private SocketOriginator _socketOriginator;
	@SuppressWarnings("unused") private SocketReceiver _socketReceiver;

	@Test
	public void turnOnline() {
		_socketReceiver = my(SocketReceiver.class);
		_socketOriginator = my(SocketOriginator.class);

		final Contact neide = _contactManager.produceContact("Neide");
		assertFalse(_subject.isConnectedTo(neide));

		_addressKeeper.add(neide, "neide.selfip.net", 6789);

		assertTrue(_subject.isConnectedTo(neide));
		assertTrue(_subject.connectionFor(neide).isOnline().currentValue());	
	}
}
