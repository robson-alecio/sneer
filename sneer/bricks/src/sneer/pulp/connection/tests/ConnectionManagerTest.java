package sneer.pulp.connection.tests;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.clock.Clock;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.network.ByteArraySocket;

@RunWith(JMock.class)
public class ConnectionManagerTest extends TestThatIsInjected {

	@Inject
	Clock _clock;
	
	@Inject
	ConnectionManager _subject;
	
	@Inject
	BlinkingLights _lights;
	
	private final Mockery _mockery = new JUnit4Mockery();
	
	@Test
	public void testBlinkingLightWhenUnreachable() throws Exception {
		
		assertEquals(0, _lights.lights().currentSize());
		
		_clock.advanceTime(30*1000);
		
		assertEquals(1, _lights.lights().currentSize());
		
		final ByteArraySocket socket = mock(ByteArraySocket.class);
		_mockery.checking(new Expectations() {{
			allowing(socket).read();
				will(returnValue(new byte[0]));
		}});
		_subject.manageIncomingSocket(mock(Contact.class), socket);
		
		assertEquals(0, _lights.lights().currentSize());
	}

	private <T>  T mock(Class<T> typeToMock) {
		return _mockery.mock(typeToMock);
	}
}
