package sneer.pulp.blinkinglights.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.clock.Clock;

public class BlinkingLightsTest extends TestThatIsInjected {

	@Inject
	private static BlinkingLights _subject;

	@Inject
	private static Clock _clock;


	@Test
	public void testLights() throws Exception {
		assertLightCount(0, _subject);
		
		Light light = _subject.turnOn(LightType.ERROR, "some error", new NullPointerException());
		assertTrue(light.isOn());
		assertEquals("some error", light.message());
		assertNotNull(light.error());
		assertLightCount(1, _subject);
		
		_subject.turnOff(light);
		assertFalse(light.isOn());
		assertLightCount(0, _subject);
	}

	private void assertLightCount(int count, BlinkingLights _lights) {
		assertEquals(count, _lights.lights().currentSize());
	}
	
	@Test
	public void testTimeout() throws Exception {
		
		final String message = "some error";
		final NullPointerException exception = new NullPointerException();
		final int timeout = 1000;

		final Light light = _subject.turnOn(LightType.ERROR, message, exception, timeout);

		assertTrue(light.isOn());
		
		_clock.advanceTime(timeout);		
		assertTrue(light.isOn());
		
		_clock.advanceTime(1);		
		assertFalse(light.isOn());
	}

}
