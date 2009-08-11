package sneer.bricks.network.computers.sockets.connections.tests;

import static sneer.foundation.environments.Environments.my;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.junit.Test;

import sneer.bricks.network.computers.sockets.connections.ConnectionManager;
import sneer.bricks.network.computers.sockets.protocol.ProtocolTokens;
import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.Seal;
import sneer.foundation.brickness.testsupport.Bind;
import sneer.foundation.brickness.testsupport.BrickTest;

public class IncomingSocketTest extends BrickTest {

	private ConnectionManager _subject = my(ConnectionManager.class);

	private final ByteArraySocket _socketB = mock(ByteArraySocket.class, "socket1");
	private final ByteArraySocket _socketA = mock(ByteArraySocket.class, "socket2");

	private final Seal _smallerSeal = new Seal(new byte[]{1, 1, 1});
	private final Seal _ownSeal     = new Seal(new byte[]{2, 2, 2});
	private final Seal _greaterSeal = new Seal(new byte[]{3, 3, 3});

	@Bind private final Seals _seals = mock(Seals.class);
	
	
	@Test (timeout = 2000)
	public void tieBreak() throws Exception {
		
		final Contact a = my(ContactManager.class).produceContact("Contact A");
		final Contact b = my(ContactManager.class).produceContact("Contact B");
		
		checking(new Expectations() {{
			Sequence sequence = newSequence("main");

			allowing(_seals).ownSeal(); will(returnValue(_ownSeal));
			allowing(_seals).contactGiven(_smallerSeal); will(returnValue(a));
			allowing(_seals).contactGiven(_greaterSeal); will(returnValue(b));
			allowing(_seals).sealGiven(a); will(returnValue(_smallerSeal));
			allowing(_seals).sealGiven(b); will(returnValue(_greaterSeal));

			oneOf(_socketA).read(); will(returnValue(ProtocolTokens.SNEER_WIRE_PROTOCOL_1)); inSequence(sequence);
			oneOf(_socketA).read(); will(returnValue(new byte[]{1, 1, 1})); inSequence(sequence);
			oneOf(_seals).unmarshall(new byte[]{1, 1, 1}); will(returnValue(_smallerSeal)); inSequence(sequence);
			oneOf(_socketA).write(ProtocolTokens.CONFIRMED); inSequence(sequence);

			oneOf(_socketB).read(); will(returnValue(ProtocolTokens.SNEER_WIRE_PROTOCOL_1)); inSequence(sequence);
			oneOf(_socketB).read(); will(returnValue(new byte[]{3, 3, 3})); inSequence(sequence);
			oneOf(_seals).unmarshall(new byte[]{3, 3, 3}); will(returnValue(_greaterSeal)); inSequence(sequence);
			oneOf(_socketB).read(); will(returnValue(ProtocolTokens.CONFIRMED)); inSequence(sequence);
			
		}});

		_subject.manageIncomingSocket(_socketA);
		_subject.manageIncomingSocket(_socketB);
	}
}