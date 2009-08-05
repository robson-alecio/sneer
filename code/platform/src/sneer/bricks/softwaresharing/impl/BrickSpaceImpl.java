package sneer.bricks.softwaresharing.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.pulp.reactive.collections.SetRegister;
import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.bricks.pulp.reactive.collections.impl.SetRegisterImpl;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickSpace;
import sneer.bricks.softwaresharing.publisher.Building;
import sneer.foundation.lang.Consumer;

class BrickSpaceImpl implements BrickSpace, Consumer<Building> {

	private SetRegister<BrickInfo> _availableBricks = new SetRegisterImpl<BrickInfo>();
	@SuppressWarnings("unused")	private final WeakContract _brickUsageContract;

	{
		my(TupleSpace.class).keep(Building.class);
		_brickUsageContract = my(TupleSpace.class).addSubscription(Building.class, this);
	}
	
	@Override
	public SetSignal<BrickInfo> availableBricks() {
		return _availableBricks.output();
	}

	@Override
	public void consume(Building building) {
		_availableBricks.addAll(new BrickFetcher(
			building.hashOfOwnBricks,
			building.hashOfPlatformBricks
		).bricks());
	}

}
