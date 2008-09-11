package sneer.pulp.blinkinglights.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.impl.LightImpl;
import sneer.pulp.clock.mocks.ClockMock;
import wheel.lang.FrozenTime;

public class BlinkingLightsTest extends TestThatIsInjected {

	@Inject
	private BlinkingLights _lights;

	final Mockery context = new JUnit4Mockery();
	final BlinkingLights lights = context.mock(BlinkingLights.class);
	final ClockMock clock = new ClockMock();
	
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
		final Light light = new LightImpl(message, exception, timeout, clock);
		
		context.checking(new Expectations() {{
			exactly(1).of(lights).turnOn(message, exception, timeout);
				will(returnValue(light));				
				
		}});
		
		FrozenTime.freezeForCurrentThread(1);
		assertEquals(light, lights.turnOn(message, exception, timeout));
		
		assertTrue(light.isOn());
		
		FrozenTime.freezeForCurrentThread(timeout);
		assertFalse(light.isOn());
	}
}
