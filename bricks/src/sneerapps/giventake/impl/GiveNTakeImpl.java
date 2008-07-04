package sneerapps.giventake.impl;

import sneer.bricks.things.Thing;
import sneerapps.giventake.GiveNTake;
import wheel.reactive.sets.SetSignal;

class GiveNTakeImpl implements GiveNTake {

	@Override
	public void advertise(Thing thing) {
		//Refactor This is wierd. This method has to do nothing because things are already "advertised" (they are directly searchable).
	}

	@Override
	public SetSignal<Thing> search(String tags) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

}
