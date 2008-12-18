package snapps.watchme.impl;

import java.awt.image.BufferedImage;
import java.util.List;

import snapps.watchme.WatchMe;
import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import snapps.watchme.codec.ImageCodec.Decoder;
import snapps.watchme.codec.ImageCodec.Encoder;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.threadpool.ThreadPool;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.screenshotter.Screenshotter;
import wheel.lang.Consumer;
import wheel.lang.exceptions.FriendlyException;
import wheel.lang.exceptions.Hiccup;
import wheel.reactive.EventSource;
import wheel.reactive.impl.EventNotifierImpl;
import static wheel.lang.Environments.my;

class WatchMeImpl implements WatchMe {

	private static final int PERIOD_IN_MILLIS = 1000;

	private final Screenshotter _shotter = my(Screenshotter.class);
	private final ImageCodec _codec = my(ImageCodec.class);
	private final TupleSpace _tupleSpace = my(TupleSpace.class);
	private final ThreadPool _pool = my(ThreadPool.class);
	private final BlinkingLights _lights = my(BlinkingLights.class);

	private final Clock _clock = my(Clock.class);
	
	private boolean _isRunning;
	private final Light _light = _lights.prepare(LightType.ERROR);

	private Encoder _encoder;

	@Override
	public EventSource<BufferedImage> screenStreamFor(final PublicKey publisher) {
		if(publisher==null) {
			throw new IllegalArgumentException("The publisher argument can't be null.");
		}
		
		final EventNotifierImpl<BufferedImage> result = new EventNotifierImpl<BufferedImage>();
		final Decoder decoder = _codec.createDecoder();
		
		_tupleSpace.addSubscription(ImageDelta.class, new Consumer<ImageDelta>(){@Override public void consume(ImageDelta delta) {
			if (!delta.publisher().equals(publisher))
				return;
			
			if (decoder.applyDelta(delta));
				result.notifyReceivers(decoder.screen());
		}});
		
		return result.output();
	}

	@Override
	public void startShowingMyScreen() {
		_isRunning = true;
		_pool.registerActor(new Runnable(){ @Override public void run() {
			while (_isRunning) {
				doPublishShot();
				_clock.sleepAtLeast(PERIOD_IN_MILLIS);
			}
			_encoder = null;
		}});
	}

	private void doPublishShot() {
		try {
			tryToPublishShot();
			_lights.turnOffIfNecessary(_light);
		} catch (Hiccup ok) {			
		} catch (FriendlyException e) {
			_lights.turnOnIfNecessary(_light, e);
		}
	}

	private void tryToPublishShot() throws Hiccup, FriendlyException {
		BufferedImage shot = _shotter.takeScreenshot();
		
		if (_encoder == null)
			_encoder = _codec.createEncoder();
		
		List<ImageDelta> deltas = _encoder.generateDeltas(shot);
		for (ImageDelta delta : deltas)
			_tupleSpace.publish(delta);
	}

	@Override
	public void stopShowingMyScreen() {
		_isRunning = false;
	}

}
