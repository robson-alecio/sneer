package snapps.wind.impl;

import snapps.wind.Shout;
import snapps.wind.TupleSpace;
import snapps.wind.Wind;
import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.KeyManager;
import wheel.lang.Omnivore;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

class WindImpl implements Wind, Omnivore<Shout> {

	@Inject
	static private TupleSpace _environment;

	@Inject
	static private Clock _clock;
	
	@Inject
	static private KeyManager _keyManager;

	private ListRegister<Shout> _shoutsHeard = new ListRegisterImpl<Shout>();

	{
		_environment.addSubscription(this, Shout.class);
	}

	@Override
	public ListSignal<Shout> shoutsHeard() {
		return _shoutsHeard.output();
	}

	@Override
	public void consume(Shout shout) {
		_shoutsHeard.add(shout);
	}

	@Override
	public Omnivore<String> megaphone() {
		return new Omnivore<String>(){ @Override public void consume(String phrase) {
			shout(phrase);
		}};
	}

	private void shout(String phrase) {
		_environment.publish(new Shout(_keyManager.ownPublicKey(), _clock.time(), phrase));
	}
	

}
