package sneer.bricks.network.social.loggers.tuples.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.network.social.loggers.tuples.TupleLogger;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.brickness.Seal;
import sneer.foundation.brickness.Tuple;
import sneer.foundation.lang.Consumer;

class TupleLoggerImpl implements TupleLogger, Consumer<Tuple> {

	{
		my(TupleSpace.class).addSubscription(Tuple.class, this);
	}

	@Override
	public void consume(Tuple tuple) {
		Seal publisherSeal = tuple.publisher();
		if (my(Seals.class).ownSeal().equals(publisherSeal))
			logPublished(tuple);
		else
			logReceived(tuple, publisherSeal);
	}

	private void logPublished(Tuple tuple) {
		my(Logger.class).log("Tuple published: {}", tuple.getClass().getSimpleName());
	}
	
	private void logReceived(Tuple tuple, Seal publisherSeal) {
		my(Logger.class).log("Tuple received: {} from: {}", tuple.getClass().getSimpleName(), my(Seals.class).contactGiven(publisherSeal));
	}

}
