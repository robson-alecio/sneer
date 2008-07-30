package sneerapps.wind.impl;

import sneerapps.wind.ConnectionSide;
import sneerapps.wind.TupleSpace;
import sneerapps.wind.Probe;
import sneerapps.wind.Tuple;
import wheel.lang.Omnivore;
import static wheel.lang.Types.cast;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;

public class ProbeImpl implements Probe {

	private class InstructionsReceiver implements Omnivore<Object> {

		@Override
		public void consume(Object instruction) {
			_minAffinity.setter().consume((Float)instruction);
		}

	}

	private final Register<Float> _minAffinity = new RegisterImpl<Float>(0f);
	private final InstructionsReceiver _instructionsReceiver = new InstructionsReceiver();
	private boolean _isStarted = false;

	@Override
	synchronized public void startProbing(TupleSpace foreignEnvironment, ConnectionSide connectionBackHome) {
		if (_isStarted) throw new IllegalStateException("Already probing...");
		_isStarted = true;
		
		Omnivore<Tuple> sender = cast(connectionBackHome.sender());

		connectionBackHome.registerReceiver(_instructionsReceiver);
		foreignEnvironment.addSubscription(sender, Tuple.class, _minAffinity.output());
	}

	
}
