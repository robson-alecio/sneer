package snapps.watchme.impl;

import java.awt.image.BufferedImage;
import java.util.List;

import snapps.watchme.WatchMe;
import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.threadpool.ThreadPool;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.screenshotter.Screenshotter;
import wheel.lang.Omnivore;
import wheel.lang.exceptions.FriendlyException;
import wheel.lang.exceptions.Hiccup;
import wheel.reactive.EventSource;
import wheel.reactive.impl.EventNotifierImpl;

class WatchMeImpl implements WatchMe {

	@Inject
	static private Screenshotter _shotter;
	@Inject
	static private ImageCodec _codec;
	@Inject
	static private TupleSpace _tupleSpace;
	@Inject
	static private ThreadPool _pool;
	@Inject
	static private BlinkingLights _lights;

	@Inject
	static private Clock _clock;
	
	private BufferedImage _previous;
	private boolean _isRunning;
	private final Light _light = _lights.prepare(LightType.ERROR, "Unable to take Screenshot");

	@Override
	public EventSource<BufferedImage> screenStreamFor(final PublicKey publisher) {
		final EventNotifierImpl<BufferedImage> result = new EventNotifierImpl<BufferedImage>();
		final BufferedImage screen = generateBlankImage(1024, 768);
		
		_tupleSpace.addSubscription(ImageDelta.class, new Omnivore<ImageDelta>(){@Override public void consume(ImageDelta delta) {
			if (delta.publisher != publisher) {
				System.out.println(delta.publisher);
				System.out.println(publisher);
				return;
			}
			_codec.applyDelta(screen, delta);
			result.notifyReceivers(screen);
		}});
		
		return result.output();
	}

	@Override
	public void startShowingMyScreen() {
		_isRunning = true;
		_pool.registerActor(new Runnable(){ @Override public void run() {
			while (_isRunning) {
				doPublishShot();
				_clock.sleepAtLeast(500);
			}
			_previous = null;
		}});

	}

	private void doPublishShot() {
		try {
			tryToPublishShot();
			_lights.turnOff(_light);
		} catch (Hiccup expected) {			
		} catch (FriendlyException e) {
			_lights.turnOnIfNecessary(_light, e);
		}
	}

	private void tryToPublishShot() throws Hiccup, FriendlyException {
		BufferedImage shot = _shotter.takeScreenshot();
		
		if (_previous == null)
			_previous = generateBlankImage(shot.getWidth(), shot.getHeight());
		
		List<ImageDelta> deltas = _codec.encodeDeltas(_previous, shot);
		for (ImageDelta delta : deltas)
			_tupleSpace.publish(delta);
		
		_previous = shot;
	}

	private BufferedImage generateBlankImage(int width, int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	@Override
	public void stopShowingMyScreen() {
		_isRunning = false;
	}

}
