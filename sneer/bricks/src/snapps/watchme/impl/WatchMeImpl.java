package snapps.watchme.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.image.BufferedImage;
import java.util.List;

import snapps.watchme.ImageDeltaPacket;
import snapps.watchme.WatchMe;
import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import snapps.watchme.codec.ImageCodec.Decoder;
import snapps.watchme.codec.ImageCodec.Encoder;
import sneer.brickness.PublicKey;
import sneer.hardware.cpu.exceptions.FriendlyException;
import sneer.hardware.cpu.exceptions.Hiccup;
import sneer.hardware.ram.arrays.ImmutableByteArray;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.clock.Clock;
import sneer.pulp.datastructures.cache.Cache;
import sneer.pulp.datastructures.cache.CacheFactory;
import sneer.pulp.events.EventNotifier;
import sneer.pulp.events.EventNotifiers;
import sneer.pulp.events.EventSource;
import sneer.pulp.threadpool.ThreadPool;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.screenshotter.Screenshotter;
import sneer.software.lang.Consumer;
import wheel.io.Logger;

class WatchMeImpl implements WatchMe {

	private static final int CACHE_CAPACITY = 0; //3000;

	private static final int PERIOD_IN_MILLIS = 1000;

	private final Screenshotter _shotter = my(Screenshotter.class);
	private final ImageCodec _codec = my(ImageCodec.class);
	private final CacheFactory _cacheFactory = my(CacheFactory.class);
	private final TupleSpace _tupleSpace = my(TupleSpace.class);
	private final ThreadPool _pool = my(ThreadPool.class);
	private final BlinkingLights _lights = my(BlinkingLights.class);

	private final Clock _clock = my(Clock.class);
	
	private boolean _isRunning;
	private final Light _light = _lights.prepare(LightType.ERROR);

	private Encoder _encoder;
	private Cache<ImmutableByteArray> _cache;

	@Override
	public EventSource<BufferedImage> screenStreamFor(final PublicKey publisher) {
		if (publisher == null)
			throw new IllegalArgumentException("The publisher argument can't be null.");
		
		EventNotifier<BufferedImage> result = my(EventNotifiers.class).create();
		
		_tupleSpace.addSubscription(ImageDeltaPacket.class, imageDeltaPacketConsumer(publisher, result));
		
		return result.output();
	}

	private Consumer<ImageDeltaPacket> imageDeltaPacketConsumer(final PublicKey publisher,	final EventNotifier<BufferedImage> notifier) {
		final Decoder decoder = _codec.createDecoder();
		final Cache<ImmutableByteArray> cache = _cacheFactory.createWithCapacity(CACHE_CAPACITY);
		
		return new Consumer<ImageDeltaPacket>(){@Override public void consume(ImageDeltaPacket delta) {
			if (!delta.publisher().equals(publisher))
				return;
			
			ImmutableByteArray imageData = delta.imageData != null
				? delta.imageData
				: cache.getByHandle(delta.cacheHandle);
			
			if (imageData == null) {
				Logger.log("Local WatchMe image cache out of sync with peer's cache.");
				return;
			}

			cache.keep(imageData);
			
			if (decoder.applyDelta(new ImageDelta(imageData, delta.x, delta.y)));
				notifier.notifyReceivers(decoder.screen());
		}};
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
			_cache = null;
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
		
		List<ImageDelta> deltas = encoder().generateDeltas(shot);
		for (ImageDelta delta : deltas)
			publish(delta);
	}

	private void publish(ImageDelta delta) {
		if (cache().contains(delta.imageData))
			publishCacheHit(delta);
		else
			_tupleSpace.publish(new ImageDeltaPacket(delta.x, delta.y, delta.imageData, 0));
		
		cache().keep(delta.imageData);
	}

	private void publishCacheHit(ImageDelta delta) {
		int handle = cache().handleFor(delta.imageData);
		_tupleSpace.publish(new ImageDeltaPacket(delta.x, delta.y, null, handle));
	}

	private Encoder encoder() {
		if (_encoder == null)
			_encoder = _codec.createEncoder();
		return _encoder;
	}

	private Cache<ImmutableByteArray> cache() {
		if (_cache == null)
			_cache = _cacheFactory.createWithCapacity(CACHE_CAPACITY);
		return _cache;
	}

	@Override
	public void stopShowingMyScreen() {
		_isRunning = false;
	}

}
