package sneer.bricks.pulp.events;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;

public interface PulseSource {
	
	Contract addPulseReceiver(Runnable pulseReceiver);

}
