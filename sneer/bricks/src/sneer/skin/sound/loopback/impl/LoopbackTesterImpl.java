package sneer.skin.sound.loopback.impl;

import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.skin.sound.loopback.LoopbackTester;
import sneer.skin.sound.mic.Mic;
import sneer.skin.sound.speaker.Speaker;

class LoopbackTesterImpl implements LoopbackTester{

	@Inject static private Mic _mic;
	@Inject static private Speaker _speaker;
	@Inject static private Clock _clock;
	
	private final int DELAY = 3000;

	@Override
	public void close() {
		_mic.close();
		_speaker.close();
	}

	@Override
	public void open() {
		_mic.open();
		_clock.wakeUpNoEarlierThan(DELAY, new Runnable(){ @Override public void run() {
			_speaker.open();
		}});
	}	
}