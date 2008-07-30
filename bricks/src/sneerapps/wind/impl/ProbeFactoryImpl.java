package sneerapps.wind.impl;

import sneer.bricks.keymanager.PublicKey;
import sneer.lego.Inject;
import sneerapps.wind.ConnectionSide;
import sneerapps.wind.TupleSpace;
import sneerapps.wind.Probe;
import sneerapps.wind.ProbeFactory;
import sneerapps.wind.Tuple;
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
