package sneer.pulp.blinkinglights.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
		
		FrozenTime.freezeForCurrentThread(1);
		assertTrue(light.isOn());
		
		FrozenTime.freezeForCurrentThread(timeout);
		assertTrue(light.isOn());
		
		FrozenTime.freezeForCurrentThread(timeout+1);
		assertFalse(light.isOn());
	}
}
