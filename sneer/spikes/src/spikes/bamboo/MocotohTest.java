package spikes.bamboo;

import static wheel.lang.Environment.my;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.pulp.clock.Clock;

@RunWith(Mocotoh.class)
public class MocotohTest extends Assert {
	@Test
	public void test() {
		assertEquals(0, my(Clock.class).time());
	}
}
