package sneer.bricks.pulp.own.name.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.software.bricks.statestore.BrickStateStore;
import sneer.foundation.lang.Consumer;

class OwnNameKeeperImpl implements OwnNameKeeper {

	private final Register<String> _name = my(Signals.class).newRegister("");
	
	@SuppressWarnings("unused")
	private final Object _refToAvoidGc;
	
	OwnNameKeeperImpl(){
		restore();
		_refToAvoidGc = my(Signals.class).receive(name(), new Consumer<String>(){ @Override public void consume(String name) {
			save(name);
		}});
	}
	
	@Override
	public Signal<String> name() {
		return _name.output();
	}

	@Override
	public Consumer<String> nameSetter() {
		return _name.setter();
	}

	@Override
	public Signal<String> nameOf(Contact contact) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}
	
	private void restore() {
		String restoredName = (String) my(BrickStateStore.class).readObjectFor(OwnNameKeeper.class, getClass().getClassLoader());
		
		if(restoredName!=null)
			nameSetter().consume(restoredName);
	}
	
	private void save(String name) {
		my(BrickStateStore.class).writeObjectFor(OwnNameKeeper.class, name);
	}
}
