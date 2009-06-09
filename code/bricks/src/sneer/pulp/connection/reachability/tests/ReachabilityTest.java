package sneer.pulp.connection.reachability.tests;

import static sneer.commons.environments.Environments.my;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.brickness.testsupport.Contribute;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.clock.Clock;
import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.connection.reachability.ReachabilitySentinel;
import sneer.pulp.events.EventNotifier;
import sneer.pulp.events.EventNotifiers;
import sneer.pulp.network.ByteArraySocket;

public class ReachabilityTest extends BrickTest {
	
	@Contribute private final SocketAccepter _accepter = mock(SocketAccepter.class);
	private final EventNotifier<ByteArraySocket> _notifier = my(EventNotifiers.class).create();
	
	private final Clock _clock = my(Clock.class);
	
	private final BlinkingLights _lights = my(BlinkingLights.class);
		
	@Test
	public void testBlinkingLightWhenUnreachable() throws Exception {
		checking(new Expectations() {{
			exactly(1).of(_accepter).lastAcceptedSocket();
				will(returnValue(_notifier.output()));
		}});

		startReachabilitySentinel();
		assertEquals(0, _lights.lights().size().currentValue().intValue());
		
		_clock.advanceTime(30*1000);
		assertEquals(1, _lights.lights().size().currentValue().intValue());
		
		ByteArraySocket socket = mock(ByteArraySocket.class);
		_notifier.notifyReceivers(socket);
		assertEquals(0, _lights.lights().size().currentValue().intValue());
	}

	private void startReachabilitySentinel() {
		my(ReachabilitySentinel.class);
	}
}
