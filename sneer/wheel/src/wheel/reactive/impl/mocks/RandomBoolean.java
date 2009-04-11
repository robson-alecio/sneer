package wheel.reactive.impl.mocks;

import static sneer.commons.environments.Environments.my;

import java.util.Random;

import sneer.commons.threads.Daemon;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import wheel.lang.Threads;

public class RandomBoolean {

	private static final Random RANDOM = new Random();
	private Register<Boolean> _register = my(Signals.class).newRegister(false);

	{
		new Daemon("Random Boolean") { @Override public void run() {
			while (true) sleepAndFlip();
		}};
	}
	
	public Signal<Boolean> output() {
		return _register.output();
	}

	private void sleepAndFlip() {
		Threads.sleepWithoutInterruptions(RANDOM.nextInt(2000));
		_register.setter().consume(!_register.output().currentValue());
	}
	
}
