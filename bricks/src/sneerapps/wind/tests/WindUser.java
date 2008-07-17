package sneerapps.wind.tests;

import sneer.bricks.keymanager.PublicKey;
import sneerapps.wind.Probe;
import sneerapps.wind.Shout;
import wheel.reactive.sets.SetSignal;


public interface WindUser {
	void connectTo(WindUser peer);
	void affinityFor(WindUser peer, float percentage);

	void shout(String string);
	void hearShoutsWithAffinityGreaterThan(float percentage);
	SetSignal<Shout> shoutsHeard();

	/////////////////////////////////////Used only by the test implementation:
	
	PublicKey publicKey();
	Probe produceProbeFor(WindUser peer);
	void receiveProbe(Probe probe);
}