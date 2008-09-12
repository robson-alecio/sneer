package sneer.pulp.blinkinglights.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.clock.realtime.mocks.RealtimeClockMock;

public class BlinkingLightsTest extends TestThatIsInjected {

	final RealtimeClockMock _mock = new RealtimeClockMock();
	final Container _container = ContainerUtils.newContainer(_mock);

	@Test
	public void testLights() throws Exception {
		
		final BlinkingLights lights = _container.produce(BlinkingLights.class);
		
		assertLightCount(0, lights);
		
		Light light = lights.turnOn(Light.ERROR_TYPE, "some error", new NullPointerException());
		assertTrue(light.isOn());
		assertEquals("some error", light.message());
		assertNotNull(light.error());
		assertLightCount(1, lights);
		
		lights.turnOff(light);
		assertFalse(light.isOn());
		assertLightCount(0, lights);
	}

	private void assertLightCount(int count, BlinkingLights _lights) {
		assertEquals(count, _lights.lights().currentSize());
	}
	
	@Test
	public void testTimeout() throws Exception {
		
		final String message = "some error";
		final NullPointerException exception = new NullPointerException();
		final int timeout = 1000;

		final BlinkingLights lights = _container.produce(BlinkingLights.class);
		final Light light = lights.turnOn(Light.ERROR_TYPE, message, exception, timeout);

		assertTrue(light.isOn());
		
		_mock.advanceTime(timeout);		
		assertTrue(light.isOn());
		
		_mock.advanceTime(1);		
		assertFalse(light.isOn());
	}
}
