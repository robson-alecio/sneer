package sneer.bricks.network.social.heartbeat.tests;

import static sneer.foundation.environments.Environments.my;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.network.social.heartbeat.Heart;
import sneer.bricks.network.social.heartbeat.Heartbeat;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.brickness.testsupport.Bind;
import sneer.foundation.brickness.testsupport.BrickTest;

public class HeartTest extends BrickTest {

	@Bind TupleSpace _tupleSpace = mock(TupleSpace.class);

	@Test
	public void heartIsBeating() {
		checking(new Expectations() {{
			exactly(4).of(_tupleSpace).publish(with(any(Heartbeat.class)));
		}});

		my(Heart.class);
		passTime();
		passTime();
		passTime();
	}

	private void passTime() {
		int moreThanEnough = 10 * 60 * 1000;
		my(Clock.class).advanceTime(moreThanEnough);
	}
	
}
