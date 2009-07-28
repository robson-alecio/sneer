package spikes.bamboo.mocotoh;

import static sneer.foundation.environments.Environments.my;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.bricks.hardware.clock.Clock;

@RunWith(Mocotoh.class)
public class MocotohTest extends Assert {
	@Test
	public void test() {
		assertEquals(new Long(0), my(Clock.class).time().currentValue());
	}
}





