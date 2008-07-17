package sneerapps.wind.tests.impl;

import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.keymanager.PublicKey;
import sneer.lego.Inject;
import sneerapps.wind.Environment;
import sneerapps.wind.Probe;
import sneerapps.wind.ProbeFactory;
import sneerapps.wind.Shout;
import sneerapps.wind.Wind;
import sneerapps.wind.tests.WindUser;
import wheel.lang.Threads;
import wheel.reactive.sets.SetSignal;

public class WindUserImpl implements WindUser {

	@Inject
	static private Wind _wind;
	
	@Inject
	static private Environment _environment;

	@Inject
	static private KeyManager _keyManager;

	@Inject
	static private ProbeFactory _probeFactory;
	
	@Override
	public void connectTo(WindUser peer) {
		Probe myProbe = produceProbeFor(peer);
		Probe hisProbe = peer.produceProbeFor(this);
		
		receiveProbe(hisProbe);
		peer.receiveProbe(myProbe);
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
	public void affinityFor(WindUser peer, float percentage) {
		_wind.hearShoutsWithAffinityGreaterThan().consume(percentage);
	}

	@Override
	public void hearShoutsWithAffinityGreaterThan(float percentage) {
		_wind.hearShoutsWithAffinityGreaterThan();
	}

	@Override
	public Probe produceProbeFor(WindUser peer) {
		return _probeFactory.produceProbeFor(peer.publicKey());
	}

	@Override
	public void receiveProbe(Probe probe) {
		Threads.preventFromBeingGarbageCollected(probe); //Fix This is a leak.
		probe.startProbing(_environment);
	}

}
