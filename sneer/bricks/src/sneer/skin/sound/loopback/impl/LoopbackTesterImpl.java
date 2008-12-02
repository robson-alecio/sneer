package sneer.skin.sound.loopback.impl;

import java.io.ByteArrayOutputStream;

import sneer.skin.sound.loopback.LoopbackTester;
import wheel.io.Logger;

class LoopbackTesterImpl implements LoopbackTester{

	@Override
	public void stop() {
		Recorder.stop();
		Player.stop();
		Logger.log("Audio Loopback Test stopped.");
	}

	@Override
	public void start() {
		Logger.log("Audio Loopback Test started.");

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		Recorder.start(buffer);
		Player.start(buffer);
	}	
}