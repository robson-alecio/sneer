package spikes.klaus.junitrunners;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TestRunnerSpike.class)
public class TestThatFails extends Assert {

	@Test
	public void test() {
		fail("Failed");
	}
	
}
