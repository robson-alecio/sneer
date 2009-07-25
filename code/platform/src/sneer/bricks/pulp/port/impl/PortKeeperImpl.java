package sneer.bricks.pulp.port.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.pulp.port.PortKeeper;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.software.bricks.statestore.BrickStateStore;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.PickyConsumer;
import sneer.foundation.lang.exceptions.NotImplementedYet;
import sneer.foundation.lang.exceptions.Refusal;

class PortKeeperImpl implements PortKeeper {

	private final BrickStateStore _store = my(BrickStateStore.class);
	private final PortNumberRegister _register = new PortNumberRegister(0);
	
	@SuppressWarnings("unused")
	private Object _refToAvoidGc;
	
	PortKeeperImpl(){
		restore();
		_refToAvoidGc = port().addReceiver(new Consumer<Integer>(){ @Override public void consume(Integer port) {
			save(port);
		}});
	}
	
	@Override
	public Signal<Integer> port() {
		return _register.output();
	}
	
	@Override
	public PickyConsumer<Integer> portSetter() {
		return _register.setter();
	}

	private void save(Integer port) {
		_store.writeObjectFor(PortKeeper.class, port);
	}
	
	private void restore() {
		Integer restoredPort = (Integer) _store.readObjectFor(PortKeeper.class, getClass().getClassLoader());
		if(restoredPort!=null)
			try {
				portSetter().consume(restoredPort);
			} catch (Refusal e) {
				throw new NotImplementedYet(e); // Fix Handle this exception.
			}
	}
}