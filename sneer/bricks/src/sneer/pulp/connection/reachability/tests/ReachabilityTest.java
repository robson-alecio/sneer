package sneer.pulp.connection.reachability.tests;

import static sneer.brickness.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.Contribute;
import sneer.brickness.testsupport.TestInBrickness;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.clock.Clock;
import sneer.pulp.connection.mocks.SocketAccepterMock;
import sneer.pulp.connection.reachability.ReachabilitySentinel;
import sneer.pulp.network.ByteArraySocket;

public class ReachabilityTest extends TestInBrickness {
	
	@Contribute
	final private SocketAccepterMock _accepter = new SocketAccepterMock();
	
	private final Clock _clock = my(Clock.class);
	
	private final BlinkingLights _lights = my(BlinkingLights.class);
	
	@SuppressWarnings("unused")
	private final ReachabilitySentinel _subject = my(ReachabilitySentinel.class);
	
	@Test
	public void testBlinkingLightWhenUnreachable() throws Exception {
		assertEquals(0, _lights.lights().currentSize());
		
		_clock.advanceTime(30*1000);
		
		assertEquals(1, _lights.lights().currentSize());
		
		final ByteArraySocket socket = mock(ByteArraySocket.class);
		_accepter._notifier.notifyReceivers(socket);
		
		assertEquals(0, _lights.lights().currentSize());
	}
}
