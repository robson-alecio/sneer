package sneer.bricks.network.social.loggers.tuples.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.network.social.loggers.tuples.TupleLogger;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.brickness.Seal;
import sneer.foundation.brickness.Tuple;
import sneer.foundation.lang.Consumer;

class TupleLoggerImpl implements TupleLogger, Consumer<Tuple> {

	@SuppressWarnings("unused")	private final WeakContract _tupleSpaceContract;

	
	{
		_tupleSpaceContract = my(TupleSpace.class).addSubscription(Tuple.class, this);
	}

	
	@Override
	public void consume(Tuple tuple) {
		Seal publisherSeal = tuple.publisher();
		if (my(Seals.class).ownSeal().equals(publisherSeal))
			logPublished(tuple);
		else
			logObserved(tuple, publisherSeal);
	}

	private void logPublished(Tuple tuple) {
		my(Logger.class).log("Tuple published: {} by: {}", tuple.getClass().getSimpleName(), my(OwnNameKeeper.class).name());
	}
	
	private void logObserved(Tuple tuple, Seal publisherSeal) {
		my(Logger.class).log("Tuple observed: {} from: {}", tuple.getClass().getSimpleName(), my(Seals.class).contactGiven(publisherSeal));
	}

}
