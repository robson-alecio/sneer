package sneer.pulp.blinkinglights.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.action.CustomAction;
import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.impl.LightImpl;
import sneer.pulp.clock.Clock;

public class BlinkingLightsTest extends TestThatIsInjected {

	@Inject
	private BlinkingLights _lights;

	final Mockery context = new JUnit4Mockery();
	final BlinkingLights blinkingLights = context.mock(BlinkingLights.class);
	final Clock clock = context.mock(Clock.class);
	
	@Test
	public void testLights() throws Exception {
		assertLightCount(0);
		
		Light light = _lights.turnOn("some error", new NullPointerException());
		assertTrue(light.isOn());
		assertEquals("some error", light.message());
		assertNotNull(light.error());
		assertLightCount(1);
		
		_lights.turnOff(light);
		assertFalse(light.isOn());
		assertLightCount(0);
	}

	private void assertLightCount(int count) {
		assertEquals(count, _lights.lights().currentSize());
	}
	
	@Test
	public void testTimeout() throws Exception {
		
		final String message = "some error";
		final NullPointerException exception = new NullPointerException();
		final int timeout = 1000;

//		context.checking(new Expectations() {{
//			exactly(1).of(clck).addAlarm(with(timeout), with(any(Runnable.class)));
//		}});

		final Runnable clockTriger[] = new Runnable[1];
		
		context.checking(new Expectations() {{
			
			exactly(1).of(clock).addAlarm(with(timeout), with(any(Runnable.class)));
				will(new CustomAction("call clock.addAlarm()"){@Override public Object invoke(Invocation invocation) {
					clockTriger[0] = (Runnable)invocation.getParameter(1);
					return null;
				}});
			
			exactly(1).of(blinkingLights).turnOff(with(any(Light.class)));
				will(new CustomAction("call blinkingLights.turnOff()"){@Override public Object invoke(Invocation invocation) {
					((Light)invocation.getParameter(0)).turnOff();
					return null;
				}});
			
			
		}});

		final Light light = new LightImpl(message, exception, timeout, clock, blinkingLights);
		
		assertTrue(light.isOn());
		
		clockTriger[0].run();
		
		assertFalse(light.isOn());
	}
}
