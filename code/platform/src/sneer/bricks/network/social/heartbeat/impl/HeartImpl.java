package sneer.bricks.network.social.heartbeat.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.network.social.heartbeat.Heart;
import sneer.bricks.network.social.heartbeat.Heartbeat;
import sneer.bricks.pulp.tuples.TupleSpace;

public class HeartImpl implements Heart {
	
	{
		my(Clock.class).wakeUpNowAndEvery(10 * 1000, new Steppable() { @Override public boolean step() {
			beat();
			return true;
		}});
	}

	private void beat() {
		my(TupleSpace.class).publish(new Heartbeat());
	}

}
