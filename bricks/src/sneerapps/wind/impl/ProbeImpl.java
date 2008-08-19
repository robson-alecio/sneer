package sneerapps.wind.impl;

import static wheel.lang.Types.cast;
import sneerapps.wind.ConnectionSide;
import sneerapps.wind.Probe;
import sneerapps.wind.Tuple;
import sneerapps.wind.TupleSpace;
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
