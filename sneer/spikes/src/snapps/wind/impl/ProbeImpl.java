package snapps.wind.impl;

import static wheel.lang.Types.cast;
import snapps.wind.ConnectionSide;
import snapps.wind.Probe;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Omnivore;

public class ProbeImpl implements Probe {

	private class InstructionsReceiver implements Omnivore<Object> {

		@Override
		public void consume(Object instruction) {
			//Implement
		}

	}

	private final InstructionsReceiver _instructionsReceiver = new InstructionsReceiver();
	private boolean _isStarted = false;

	@Override
	synchronized public void startProbing(TupleSpace foreignEnvironment, ConnectionSide connectionBackHome) {
		if (_isStarted) throw new IllegalStateException("Already probing...");
		_isStarted = true;
		
		Omnivore<Tuple> sender = cast(connectionBackHome.sender());

		connectionBackHome.registerReceiver(_instructionsReceiver);
		foreignEnvironment.addSubscription(sender, Tuple.class);
	}

	
}
