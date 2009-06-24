package sneer.bricks.skin.audio.loopback.impl;

import java.io.ByteArrayOutputStream;

import sneer.bricks.pulp.log.Logger;
import sneer.bricks.skin.audio.loopback.LoopbackTester;
import static sneer.foundation.environments.Environments.my;


class LoopbackTesterImpl implements LoopbackTester{

	@Override
	public void stop() {
		Recorder.stop();
		Player.stop();
		my(Logger.class).log("Audio Loopback Test stopped.");
	}

	@Override
	public boolean start() {
		my(Logger.class).log("Audio Loopback Test started.");

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		boolean isRunning = Player.start(buffer) & Recorder.start(buffer);
		if(!isRunning) stop();
		return isRunning;
	}	
}