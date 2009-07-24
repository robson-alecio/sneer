package sneer.bricks.pulp.events;

public interface Pulser {

	PulseSource output();
	
	/** Sends a pulse to all receivers of output() (runs them). */
	void sendPulse();
	
}
