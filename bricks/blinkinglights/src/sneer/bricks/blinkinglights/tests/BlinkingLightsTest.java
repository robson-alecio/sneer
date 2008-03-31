package sneer.bricks.blinkinglights.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import sneer.bricks.blinkinglights.BlinkingLights;
import sneer.bricks.blinkinglights.Light;
import sneer.lego.Brick;
import sneer.lego.tests.BrickTestSupport;
import wheel.lang.Threads;

public class BlinkingLightsTest extends BrickTestSupport {

	@Brick
	private BlinkingLights _lights;
	
	@Test
	public void testLights() throws Exception {
		List<Light> lights = _lights.listLights();
		assertEquals(0, lights.size());
		
		Light light = _lights.turnOn("some error", new NullPointerException());
		assertTrue(light.isOn());
		assertEquals("some error", light.message());
		assertNotNull(light.error());
		lights = _lights.listLights();
		assertEquals(1, lights.size());
		
		_lights.turnOff(light);
		assertFalse(light.isOn());
		lights = _lights.listLights();
		assertEquals(0, lights.size());
	}

	@Test
	@Ignore
	public void testTimeout() throws Exception {
		List<Light> lights = _lights.listLights();
		assertEquals(0, lights.size());
		
		Light light = _lights.turnOn("some error", new NullPointerException(), 1000);
		assertTrue(light.isOn());
		Threads.sleepWithoutInterruptions(1001);
		assertFalse(light.isOn());
	}

}
