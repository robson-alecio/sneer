package snapps.wind.tests.impl;

import snapps.wind.ConnectionSide;
import snapps.wind.Probe;
import snapps.wind.ProbeFactory;
import snapps.wind.Shout;
import snapps.wind.TupleSpace;
import snapps.wind.Wind;
import snapps.wind.tests.DeepCopyingConnection;
import snapps.wind.tests.WindUser;
import sneer.kernel.container.Inject;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import wheel.io.serialization.DeepCopier;
import wheel.lang.Threads;
import wheel.reactive.Signal;
import wheel.reactive.impl.Adder;
import wheel.reactive.sets.SetSignal;

public class WindUserImpl implements WindUser {

	@Inject
	static private Wind _wind;
	
	@Inject
	static private TupleSpace _environment;

	@Inject
	static private KeyManager _keyManager;

	@Inject
	static private ProbeFactory _probeFactory;

	private String _name;
	
	@Override
	public Signal<Integer> connectAndCountTrafficTo(WindUser peer) {
		Signal<Integer> counter1 = unidirectionalConnect(this, peer);
		Signal<Integer> counter2 = unidirectionalConnect(peer, this);
		
		return new Adder(counter1, counter2).output();
	}


	private Signal<Integer> unidirectionalConnect(WindUser a, WindUser b) {
		DeepCopyingConnection connection = new DeepCopyingConnection(a.name(), b.name());
		
		Probe probe = a.createProbeFor(b, connection.sideA());
		Probe copy = DeepCopier.deepCopy(probe);
		b.receiveProbe(copy, connection.sideB());
		
		return connection.trafficCounter();
	}


	@Override
	public void shout(String phrase) {
		_wind.shout(phrase);
	}

	@Override
	public SetSignal<Shout> shoutsHeard() {
		return _wind.shoutsHeard();
	}

	@Override
	public PublicKey publicKey() {
		return _keyManager.ownPublicKey();
	}

	@Override
	public Probe createProbeFor(WindUser peer, ConnectionSide localSide) {
		return _probeFactory.createProbeFor(peer.publicKey(), localSide);
	}

	@Override
	public void receiveProbe(Probe probe, ConnectionSide localSide) {
		Threads.preventFromBeingGarbageCollected(probe); //Fix This is a leak.
		probe.startProbing(_environment, localSide);
	}


	@Override
	public String name() {
		return _name;
	}
	@Override
	public void name(String newName) {
		_name = newName;
	}

}
