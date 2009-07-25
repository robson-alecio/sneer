package sneer.bricks.pulp.events.pulsers;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;

public interface PulseSource {
	
	WeakContract addPulseReceiver(Runnable pulseReceiver);

}
