package snapps.wind.tests;

import snapps.wind.ConnectionSide;
import snapps.wind.Probe;
import snapps.wind.Shout;
import sneer.pulp.keymanager.PublicKey;
import wheel.reactive.Signal;
import wheel.reactive.sets.SetSignal;


public interface WindUser {
	
	/////////////////////////////////////Used by the test:
	
	void name(String newName);
	
	Signal<Integer> connectAndCountTrafficTo(WindUser peer);

	void shout(String string);
	SetSignal<Shout> shoutsHeard();

	
	/////////////////////////////////////Used only by the test implementation:
	
	String name();
	PublicKey publicKey();
	Probe createProbeFor(WindUser peer, ConnectionSide localSide);
	void receiveProbe(Probe probe, ConnectionSide localSide);
}