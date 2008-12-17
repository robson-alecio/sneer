package sneer.pulp.connection.reachability.tests;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.clock.Clock;
import sneer.pulp.connection.mocks.SocketAccepterMock;
import sneer.pulp.connection.reachability.ReachabilitySentinel;
import sneer.pulp.network.ByteArraySocket;
import tests.Contribute;
import tests.TestInContainerEnvironment;
import static wheel.lang.Environments.my;

public class ReachabilityTest extends TestInContainerEnvironment {
	
	@Contribute
	final private SocketAccepterMock _accepter = new SocketAccepterMock();
	
	private Clock _clock = my(Clock.class);
	
	private BlinkingLights _lights = my(BlinkingLights.class);
	
	@SuppressWarnings("unused")
	private ReachabilitySentinel _subject = my(ReachabilitySentinel.class);
	
	final private Mockery _mockery = new JUnit4Mockery();
	

	@Test
	public void testBlinkingLightWhenUnreachable() throws Exception {
		assertEquals(0, _lights.lights().currentSize());
		
		_clock.advanceTime(30*1000);
		
		assertEquals(1, _lights.lights().currentSize());
		
		final ByteArraySocket socket = mock(ByteArraySocket.class);
		_accepter._notifier.notifyReceivers(socket);
		
		assertEquals(0, _lights.lights().currentSize());
	}
	
	private <T>  T mock(Class<T> typeToMock) {
		return _mockery.mock(typeToMock);
	}	

}
