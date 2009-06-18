package sneer.bricks.pulp.connection.reachability.tests;

import static sneer.foundation.environments.Environments.my;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.connection.SocketAccepter;
import sneer.bricks.pulp.connection.reachability.ReachabilitySentinel;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.brickness.testsupport.Contribute;

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
