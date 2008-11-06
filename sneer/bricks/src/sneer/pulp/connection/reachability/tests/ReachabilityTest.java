package sneer.pulp.connection.reachability.tests;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.clock.Clock;
import sneer.pulp.connection.mocks.SocketAccepterMock;
import sneer.pulp.connection.reachability.ReachabilitySentinel;
import sneer.pulp.network.ByteArraySocket;
import wheel.io.Logger;

public class ReachabilityTest extends TestThatIsInjected {
	
	@Inject
	private static Clock _clock;
	
	@Inject
	private static BlinkingLights _lights;
	
	@SuppressWarnings("unused")
	@Inject
	private static ReachabilitySentinel _subject;
	
	final private Mockery _mockery = new JUnit4Mockery();
	
	final private SocketAccepterMock _accepter = new SocketAccepterMock();
	
	@Override
	protected Object[] getBindings() {
		return new Object[] {_accepter};
	}

	@Test
	public void testBlinkingLightWhenUnreachable() throws Exception {
		
		Logger.redirectTo(System.out);
		
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