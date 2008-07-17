package sneerapps.wind.impl;

import sneerapps.wind.Environment;
import sneerapps.wind.Probe;
import wheel.lang.Omnivore;

public class ProbeImpl implements Probe, Omnivore<Object> {

	private final Omnivore<Object> _connectionBackHome;

	ProbeImpl(Omnivore<Object> connectionBackHome) {
		_connectionBackHome = connectionBackHome;
	}
	
	@Override
	public void startProbing(Environment foreignEnvironment) {
		foreignEnvironment.addSubscriber(this, Object.class, null);
	}

	@Override
	public void consume(Object tuple) {
		_connectionBackHome.consume(tuple);
	}

	
}
