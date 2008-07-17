package sneerapps.wind.impl;

import sneer.bricks.keymanager.KeyManager;
import sneer.lego.Inject;
import sneerapps.wind.Environment;
import sneerapps.wind.Shout;
import sneerapps.wind.Wind;
import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;
import wheel.reactive.sets.SetRegister;
import wheel.reactive.sets.SetSignal;
import wheel.reactive.sets.impl.SetRegisterImpl;

class WindImpl implements Wind, Omnivore<Shout> {

	@Inject
	static private Environment _environment;
	
	@Inject
	static private KeyManager _keyManager;

	private Register<Float> _minAffinity = new RegisterImpl<Float>(0f);

	private SetRegister<Shout> _shoutsHeard = new SetRegisterImpl<Shout>();

	{
		_environment.addSubscriber(this, Shout.class, _minAffinity.output());
	}

	@Override
	public void shout(String phrase) {
		_environment.publish(new Shout(phrase, _keyManager.ownPublicKey()));
	}

	@Override
	public SetSignal<Shout> shoutsHeard() {
		return 	_shoutsHeard.output();
	}

	@Override
	public Omnivore<Float> hearShoutsWithAffinityGreaterThan() {
		return _minAffinity.setter();
	}

	@Override
	public void consume(Shout shout) {
		_shoutsHeard.add(shout);
	}


}
