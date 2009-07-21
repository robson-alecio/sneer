package sneer.bricks.software.sharing.impl;

import sneer.bricks.pulp.reactive.collections.SetRegister;
import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.bricks.pulp.reactive.collections.impl.SetRegisterImpl;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.software.sharing.BrickInfo;
import sneer.bricks.software.sharing.BrickUniverse;
import sneer.bricks.software.sharing.publisher.BrickUsage;
import sneer.foundation.lang.Consumer;
import static sneer.foundation.environments.Environments.my;

class BrickUniverseImpl implements BrickUniverse, Consumer<BrickUsage> {

	private SetRegister<BrickInfo> _availableBricks = new SetRegisterImpl<BrickInfo>();

	{
		//my(TupleSpace.class).keep(BrickUsage.class);
		my(TupleSpace.class).addSubscription(BrickUsage.class, this);
	}
	
	@Override
	public SetSignal<BrickInfo> availableBricks() {
		return _availableBricks.output();
	}

	@Override
	public void consume(BrickUsage brickUsage) {
		_availableBricks.add(new BrickInfoImpl(brickUsage.brickName));
	}

}
