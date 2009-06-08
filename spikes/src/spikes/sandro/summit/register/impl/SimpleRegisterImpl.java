package spikes.sandro.summit.register.impl;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import spikes.sandro.summit.register.SimpleRegister;
import static sneer.commons.environments.Environments.my;

class SimpleRegisterImpl implements SimpleRegister{

	private final Register<String> _register = my(Signals.class).newRegister("Olá Mundo!");
	
	@Override
	public Signal<String> output(){
		return _register.output();
	}

	@Override
	public Consumer<String> setter() {
		return _register.setter();
	}
}
