package sneer.bricks.pulp.own.name.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.lang.Consumer;

class OwnNameKeeperImpl implements OwnNameKeeper {

//	private final TupleSpace _space = my(TupleSpace.class); {
//		_space.keep(OwnName.class);
//		_space.addSubscription(OwnName.class, ownNameSubscriber());
//	}
	
	private Register<String> _name = my(Signals.class).newRegister("");
	
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
	
//	private Consumer<? super OwnName> ownNameSubscriber() {
//		return new Consumer<OwnName>() { @Override public void consume(OwnName value) {
//			// Implement
//		}};
//	}

}
