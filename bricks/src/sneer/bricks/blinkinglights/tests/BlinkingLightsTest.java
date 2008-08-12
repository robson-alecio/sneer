package sneer.bricks.blinkinglights.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sneer.bricks.blinkinglights.BlinkingLights;
import sneer.bricks.blinkinglights.Light;
import sneer.lego.Inject;
import sneer.lego.tests.BrickTestSupport;
import wheel.lang.FrozenTime;

public class BlinkingLightsTest extends BrickTestSupport {

	@Inject
	private BlinkingLights _lights;
	
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

	@Test
	public void testTimeout() throws Exception {
		assertLightCount(0);
		
		FrozenTime.freezeForCurrentThread(1);
		Light light = _lights.turnOn("some error", new NullPointerException(), 1000);
		assertTrue(light.isOn());
		
		FrozenTime.freezeForCurrentThread(1000);
		assertTrue(light.isOn());
		assertLightCount(1);

		FrozenTime.freezeForCurrentThread(1001);
		assertFalse(light.isOn());
		assertLightCount(0);

	}

	private void assertLightCount(int count) {
		assertEquals(count, _lights.listLights().size());
	}

}
