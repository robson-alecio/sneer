package spikes.wheel.reactive.impl.mocks;

import static sneer.commons.environments.Environments.my;

import java.util.Random;

import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.threads.Stepper;
import sneer.pulp.threads.Threads;

public class RandomBoolean {

	private static final Random RANDOM = new Random();
	private Register<Boolean> _register = my(Signals.class).newRegister(false);

	{
		my(Threads.class).registerStepper(new Stepper() { @Override public boolean step() {
			sleepAndFlip();
			return true;
		}});
	}
	
	public Signal<Boolean> output() {
		return _register.output();
	}

	private void sleepAndFlip() {
		my(Threads.class).sleepWithoutInterruptions(RANDOM.nextInt(5000));
		_register.setter().consume(!_register.output().currentValue());
	}
	
}
