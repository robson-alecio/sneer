package sneerapps.wind.impl;

import sneer.bricks.keymanager.KeyManager;
import sneer.lego.Inject;
import sneerapps.wind.Environment;
import sneerapps.wind.Shout;
import sneerapps.wind.Wind;
import wheel.lang.Omnivore;
import wheel.reactive.sets.SetRegister;
import wheel.reactive.sets.SetSignal;
import wheel.reactive.sets.SetSignal.SetValueChange;
import wheel.reactive.sets.impl.SetRegisterImpl;

class WindImpl implements Wind, Omnivore<SetValueChange<Shout>> {

	@Inject
	static private Environment _environment;
	
	private final SetRegister<String> _shoutsHeard = new SetRegisterImpl<String>();

	@Inject
	static private KeyManager _keyManager;

	{
		_environment.subscribe(Shout.class).addSetReceiver(this);
	}
	
	@Override
	public void shout(String phrase) {
		_environment.publish(new Shout(phrase, _keyManager.ownPublicKey()));
	}

	@Override
	public SetSignal<String> shoutsHeard() {
		return _shoutsHeard.output();
	}

	@Override
	public void consume(SetValueChange<Shout> shouts) {
		for (Shout shout : shouts.elementsAdded())
			_shoutsHeard.add(shout._phrase);

		for (Shout shout : shouts.elementsRemoved())
			_shoutsHeard.remove(shout._phrase);
	}

}
