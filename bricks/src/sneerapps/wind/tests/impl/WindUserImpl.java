package sneerapps.wind.tests.impl;

import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.keymanager.PublicKey;
import sneer.lego.Inject;
import sneerapps.wind.AffinityManager;
import sneerapps.wind.ConnectionSide;
import sneerapps.wind.TupleSpace;
import sneerapps.wind.Probe;
import sneerapps.wind.ProbeFactory;
import sneerapps.wind.Shout;
import sneerapps.wind.Wind;
import sneerapps.wind.tests.DeepCopyingConnection;
import sneerapps.wind.tests.WindUser;
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
	static private AffinityManager _affinityManager;

	@Inject
	static private ProbeFactory _probeFactory;
	
	@Override
	public Signal<Integer> connectAndCountTrafficTo(WindUser peer) {
		Signal<Integer> counter1 = unidirectionalConnect(this, peer);
		Signal<Integer> counter2 = unidirectionalConnect(peer, this);
		
		this.setAffinityFor(peer, 10f);
		peer.setAffinityFor(this, 10f);

		return new Adder(counter1, counter2).output();
	}


	private Signal<Integer> unidirectionalConnect(WindUser a, WindUser b) {
		DeepCopyingConnection connection = new DeepCopyingConnection();
		
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
	public void setAffinityFor(WindUser peer, float percentage) {
		_affinityManager.setAffinityFor(peer.publicKey(), percentage);
	}

	@Override
	public float affinityFor(WindUser peer) {
		return _affinityManager.affinityFor(peer.publicKey());
	}
	
	@Override
	public void hearShoutsWithAffinityGreaterThan(float percentage) {
		_wind.minAffinityForHearingShouts().consume(percentage);
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

}
