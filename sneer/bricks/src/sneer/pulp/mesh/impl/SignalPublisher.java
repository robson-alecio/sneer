package sneer.pulp.mesh.impl;

import sneer.kernel.container.Brick;

interface SignalPublisher {

	void subscribeTo(Class<? extends Brick> brickInterface, String signalName);

}
