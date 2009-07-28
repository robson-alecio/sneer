package sneer.bricks.softwaresharing.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.pulp.reactive.collections.SetRegister;
import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.bricks.pulp.reactive.collections.impl.SetRegisterImpl;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickUniverse;
import sneer.bricks.softwaresharing.publisher.BrickUsage;
import sneer.foundation.lang.Consumer;

class BrickUniverseImpl implements BrickUniverse, Consumer<BrickUsage> {

	private SetRegister<BrickInfo> _availableBricks = new SetRegisterImpl<BrickInfo>();
	@SuppressWarnings("unused")	private final WeakContract _tupleSpaceContract;

	{
		//my(TupleSpace.class).keep(BrickUsage.class);
		_tupleSpaceContract = my(TupleSpace.class).addSubscription(BrickUsage.class, this);
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
