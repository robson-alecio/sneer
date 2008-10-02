package snapps.watchme.impl;

import java.awt.image.BufferedImage;

import snapps.watchme.WatchMe;
import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.PublicKey;
import sneer.skin.screenshotter.Screenshotter;
import wheel.reactive.EventSource;
import wheel.reactive.impl.EventNotifierImpl;

class WatchMeImpl implements WatchMe {

	@Inject
	static private Screenshotter _shotter;

	@Inject
	static private Clock _clock;
	
	private final EventNotifierImpl<BufferedImage> _screens = new EventNotifierImpl<BufferedImage>();

	@Override
	public EventSource<BufferedImage> screenStreamFor(PublicKey key) {
		return _screens.output();
	}

	@Override
	public void startShowingMyScreen() {
		publishShot();
		_clock.wakeUpEvery(500, new Runnable(){ @Override public void run() {
			publishShot();
		}});
	}

	private void publishShot() {
		BufferedImage shot = _shotter.takeScreenshot();
		_screens.notifyReceivers(shot);
	}

	@Override
	public void stopShowingMyScreen() {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

}
