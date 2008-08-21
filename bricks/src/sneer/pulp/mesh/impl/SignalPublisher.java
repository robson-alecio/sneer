package sneer.pulp.mesh.impl;

import sneer.kernel.container.Brick;


public interface SignalPublisher {

	void subscribeTo(Class<? extends Brick> brickInterface, String signalName);
	

}
