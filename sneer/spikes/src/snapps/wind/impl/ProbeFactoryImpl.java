package snapps.wind.impl;

import snapps.wind.ConnectionSide;
import snapps.wind.Probe;
import snapps.wind.ProbeFactory;
import sneer.kernel.container.Inject;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

public class ProbeFactoryImpl implements ProbeFactory {

	private class TupleReceiver implements Omnivore<Object> { @Override public void consume(Object tuple) {
		_environment.publish((Tuple)tuple);
	}}
	
	@Inject
	static private TupleSpace _environment;


	@Override
	public Probe createProbeFor(PublicKey peerPK, ConnectionSide mySide) {
		mySide.registerReceiver(createReceiver());
		return new ProbeImpl();
	}


	private TupleReceiver createReceiver() {
		TupleReceiver receiver = new TupleReceiver();
		Threads.preventFromBeingGarbageCollected(receiver); //Fix this is a leak;
		return receiver;
	}

}
