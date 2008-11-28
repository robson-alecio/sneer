package sneer.skin.sound.mic.impl;

import java.util.concurrent.atomic.AtomicInteger;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.retrier.Retrier;
import sneer.pulp.retrier.RetrierManager;
import sneer.pulp.retrier.Task;
import sneer.pulp.threadpool.ThreadPool;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.mic.Mic;
import wheel.lang.Threads;
import wheel.lang.exceptions.FriendlyException;

public class MicImpl implements Mic {

	@Inject static private ThreadPool _threads;
	@Inject static private RetrierManager _retriers;
	@Inject static private TupleSpace _tupleSpace;
	
	@Inject private static Audio _audio;
	@Inject private static BlinkingLights _lights;
	private final Light _light = _lights.prepare(LightType.ERROR);
	
	private TargetDataLine _line;	
	private MicWorker _worker;
	private boolean _isOpen;
	
	private Object _monitor = new Object();
	
	
	@Override
	public void open() {
		synchronized (_monitor) {
			_isOpen = true;
			startToWorkIfNecessary();
		}
	}
	
	@Override
	public void close() {
		synchronized (_monitor) {
			stopAndCloseLine();
			if (_worker == null) return;
			_worker.wakeUp();
			_worker = null;
		}
	}

	private void stopAndCloseLine() {
		if (_line != null) {
			_line.stop();
			_line.drain();
			_line.close();
			_line = null;		
			_isOpen = false;
		}
	}

	private void startToWorkIfNecessary() {
		if (_worker != null) return;
			_worker = new MicWorker();
		_threads.registerActor(_worker);
	}

	private boolean isOpen() {
		return _isOpen;
	}

	private class MicWorker implements Runnable{
		
		private final MicReader _reader = new MicReader();
		
		@Override
		public void run() {
			while (true) {
				if (doCapture()) continue;
				if (doAcquireLine()) continue;
				close();
			}		
		}
		
		private boolean doCapture() {
			PcmSoundPacket result;
			synchronized (_monitor) {
				if (!isOpen()) return false;
				if (_line == null) return false;
				result = _reader.read();
			}
			_tupleSpace.publish(result);
			return true;
		}
		
		private boolean doAcquireLine() {
			Retrier retrier;
			synchronized (_monitor) {
				if (!isOpen()) return false;

				retrier = _retriers.startRetrier(5000, new Task() { @Override public void execute() {
					try {
						ensureLineIsOpen();
					} catch (LineUnavailableException e) {
						_lights.turnOnIfNecessary(_light, 
								new FriendlyException(e, "Error: audio line is unavailable, can't record!", 
						  									  "Get an expert sovereign friend to help you."));
						close();
					}	
					wakeUp();
				}});
			
				goToSleep();
			}
			
			retrier.giveUpIfStillTrying();
			return isOpen();
		}

		private void goToSleep() {
			Threads.waitWithoutInterruptions(_monitor);
		}

		synchronized private void wakeUp() {
			notify();
		}
		
		private void ensureLineIsOpen() throws LineUnavailableException {
			if(_line ==null)
				_line = _audio.openTargetDataLine();
			if (_line.isActive()) return;
		}
	}
	
	private class MicReader {
		private static final int SAMPLE_RATE_IN_HZ = 8000;
		private static final int SAMPLE_SIZE_IN_BITS = 16;
		private static final int NUMBER_OF_CHANNELS = 2;
		private static final int ONE_FIFTIETH_OF_A_SECOND = SAMPLE_RATE_IN_HZ / 50;	
		
		private PcmSoundPacket read() {
			byte[] pcmBuffer = new byte[	SAMPLE_SIZE_IN_BITS / 8 *
			                            		NUMBER_OF_CHANNELS *
			                            		ONE_FIFTIETH_OF_A_SECOND ];
			
			int read = _line.read(pcmBuffer, 0, pcmBuffer.length);
			return PcmSoundPacket.newInstance(pcmBuffer, read, nextShort());
		}
		
		private final AtomicInteger _ids = new AtomicInteger();
		private short nextShort() {
			if(_ids.compareAndSet(Short.MAX_VALUE, Short.MIN_VALUE))
				return Short.MIN_VALUE;
			return (short)_ids.incrementAndGet();
		}		
	}
}