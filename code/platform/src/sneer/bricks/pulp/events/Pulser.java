package sneer.bricks.pulp.events;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;

public interface Pulser {

	Contract addReceiver(Runnable pulseReceiver);

}
