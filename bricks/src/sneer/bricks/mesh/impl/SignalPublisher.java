package sneer.bricks.mesh.impl;

import sneer.lego.Brick;


interface SignalPublisher {

	void subscribeTo(Class<? extends Brick> brickInterface, String signalName);
	

}
