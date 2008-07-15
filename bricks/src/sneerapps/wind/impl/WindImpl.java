package sneerapps.wind.impl;

import sneer.bricks.keymanager.KeyManager;
import sneer.lego.Inject;
import sneerapps.wind.Environment;
import sneerapps.wind.Shout;
import sneerapps.wind.Wind;
import wheel.reactive.sets.SetSignal;

class WindImpl implements Wind {

	@Inject
	static private Environment _environment;
	
	@Inject
	static private KeyManager _keyManager;


	@Override
	public void shout(String phrase) {
		_environment.publish(new Shout(phrase, _keyManager.ownPublicKey()));
	}

	@Override
	public SetSignal<Shout> shoutsHeard() {
		return 	_environment.subscribe(Shout.class);
	}


}
