package sneer.bricks.pulp.events.pulsers;


public interface Pulser {

	PulseSource output();
	
	/** Sends a pulse to all receivers of output() (runs them). */
	void sendPulse();
	
}
