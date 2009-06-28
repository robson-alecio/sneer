package sneer.bricks.network.computers.sockets.connections;

import static sneer.foundation.environments.Environments.my;

import org.junit.Ignore;
import org.junit.Test;

import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.SignalUtils;
import sneer.foundation.brickness.testsupport.BrickTest;

public class ConnectionManagerTest extends BrickTest {

	private final ConnectionManager _subject = my(ConnectionManager.class);

	private final ContactManager _contactManager = my(ContactManager.class);

	@Ignore
	@Test (timeout = 2000)
	public void turnOnline() {
		final Contact neide = _contactManager.produceContact("Neide");

		Signal<Boolean> isOnline = _subject.connectionFor(neide).isOnline();
		assertFalse(isOnline.currentValue());
		
		//Implement: MOCK THE INCOMING OR OUTGOING CONNECTIONS TO NEIDE. 

		my(SignalUtils.class).waitForValue(true, isOnline);
	}
}
