package sneer.bricks.pulp.own.name.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.software.bricks.statestore.BrickStateStore;
import sneer.foundation.lang.Consumer;

class OwnNameKeeperImpl implements OwnNameKeeper {

	private final Register<String> _name = my(Signals.class).newRegister("");
	
	@SuppressWarnings("unused")
	private Object _refToAvoidGc;
	
	OwnNameKeeperImpl(){
		takeCareOfPersistence();
	}

	@Override
	public Signal<String> name() {
		return _name.output();
	}

	@Override
	public Consumer<String> nameSetter() {
		return _name.setter();
	}

	
	private void takeCareOfPersistence() {
		restore();
		
		_refToAvoidGc = name().addReceiver(new Consumer<String>(){ @Override public void consume(String name) {
			save(name);
		}});
	}

	private void restore() {
		String restoredName = (String) my(BrickStateStore.class).readObjectFor(OwnNameKeeper.class, getClass().getClassLoader());
		if (restoredName == null) return;

		nameSetter().consume(restoredName);
	}
	
	private void save(String name) {
		my(BrickStateStore.class).writeObjectFor(OwnNameKeeper.class, name);
	}
}
