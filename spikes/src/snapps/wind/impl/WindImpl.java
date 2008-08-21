package snapps.wind.impl;

import snapps.wind.Shout;
import snapps.wind.TupleSpace;
import snapps.wind.Wind;
import sneer.kernel.container.Inject;
import sneer.pulp.keymanager.KeyManager;
import wheel.lang.FrozenTime;
import wheel.lang.Omnivore;
import wheel.reactive.sets.SetRegister;
import wheel.reactive.sets.SetSignal;
import wheel.reactive.sets.impl.SetRegisterImpl;

class WindImpl implements Wind, Omnivore<Shout> {

	@Inject
	static private TupleSpace _environment;
	
	@Inject
	static private KeyManager _keyManager;

	private SetRegister<Shout> _shoutsHeard = new SetRegisterImpl<Shout>();

	{
		_environment.addSubscription(this, Shout.class);
	}

	@Override
	public void shout(String phrase) {
		_environment.publish(new Shout(_keyManager.ownPublicKey(), FrozenTime.frozenTimeMillis(), phrase));
	}

	@Override
	public SetSignal<Shout> shoutsHeard() {
		return 	_shoutsHeard.output();
	}

	@Override
	public void consume(Shout shout) {
		_shoutsHeard.add(shout);
		System.out.println("Shout heard: " + shout);
	}


}
