package sneerapps.wind.tests;

import sneer.bricks.keymanager.PublicKey;
import sneerapps.wind.ConnectionSide;
import sneerapps.wind.Probe;
import sneerapps.wind.Shout;
import wheel.reactive.Signal;
import wheel.reactive.sets.SetSignal;


public interface WindUser {
	
	/////////////////////////////////////Used by the test:
	
	void name(String newName);
	
	Signal<Integer> connectAndCountTrafficTo(WindUser peer);
	void setAffinityFor(WindUser peer, float percentage);
	float affinityFor(WindUser peer);

	void shout(String string);
	void hearShoutsWithAffinityGreaterThan(float percentage);
	SetSignal<Shout> shoutsHeard();

	
	/////////////////////////////////////Used only by the test implementation:
	
	String name();
	PublicKey publicKey();
	Probe createProbeFor(WindUser peer, ConnectionSide localSide);
	void receiveProbe(Probe probe, ConnectionSide localSide);
}