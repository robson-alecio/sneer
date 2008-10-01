package sneer.skin.sound.mic.impl;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import sneer.apps.talk.audio.AudioCommon;
import sneer.kernel.container.Inject;
import sneer.pulp.retrier.Retrier;
import sneer.pulp.retrier.RetrierManager;
import sneer.pulp.retrier.Task;
import sneer.pulp.threadpool.ThreadPool;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.mic.Mic;
import wheel.lang.Threads;
import wheel.lang.exceptions.FriendlyException;

public class MicImpl implements Mic {

	private static final int SAMPLE_RATE_IN_HZ = 8000;
	private static final int SAMPLE_SIZE_IN_BITS = 16;
	private static final int NUMBER_OF_CHANNELS = 2;
	private static final int ONE_HUNDRETH_OF_A_SECOND = SAMPLE_RATE_IN_HZ / 100;

	@Inject
	static private RetrierManager _retriers;
	@Inject
	static private ThreadPool _threads;
	@Inject
	static private TupleSpace _tupleSpace;
	
	private TargetDataLine _line;
	private final Object _lineMonitor = new Object();

	private boolean _isOpen = false;
	private final Object _stateMonitor = new Object();
	private Retrier _retrier;

	{
		_threads.registerActor(new Runnable(){ @Override public void run() {
			while (true) step();
		}});
	}

	@Override
	public void close() {
		synchronized (_stateMonitor) {
			if (!_isOpen) return;
			_isOpen = false;
			_stateMonitor.notify();
		}
	}

	@Override
	public void open() {
		synchronized (_stateMonitor) {
			if (_isOpen) return;
			_isOpen = true;
			_stateMonitor.notify();
		}
	}

	private void step() {
		boolean isOpen;
		synchronized (_stateMonitor) {
			isOpen = _isOpen;
		}
		
		if (isOpen)
			stepOpen();
		else
			stepClosed();
	}

	private void stepOpen() {
		boolean lineReady;
		synchronized (_lineMonitor) {
			lineReady = _line != null;
		}
		
		if (lineReady)
			captureAFrame();
		else
			waitForLine();
	}

	private void stepClosed() {
		closeRetrierIfNecessary();
		closeLineIfNecessary();
		waitWhileClosed();
	}


	private void closeRetrierIfNecessary() {
		synchronized (_lineMonitor) {
			if (_retrier == null) return;
			_retrier.giveUp();
			_retrier = null;
		}
	}

	private void closeLineIfNecessary() {
		synchronized (_lineMonitor) {
			if (_line == null) return;
			_line.close();
			_line = null;
		}
	}


	private void waitForLine() {
		synchronized (_lineMonitor) {
			if (_retrier != null) return;
			_retrier = _retriers.startRetrier(5000, new Task() { @Override public void execute() throws FriendlyException {
				tryToOpenLine();
				synchronized (_lineMonitor) {
					_retrier = null;
				}
			}});
		}
	}

	private void waitWhileClosed() {
		synchronized (_stateMonitor) {
			while (!_isOpen)
				Threads.waitWithoutInterruptions(_stateMonitor);
		}
	}
	
	
	private void captureAFrame() {

		byte[] pcmBuffer = new byte[
			SAMPLE_SIZE_IN_BITS / 8 *
			NUMBER_OF_CHANNELS *
			ONE_HUNDRETH_OF_A_SECOND
		];

		int read = _line.read(pcmBuffer, 0, pcmBuffer.length);

		_tupleSpace.publish(new PcmSoundPacket(pcmBuffer, read));
		
	}

	private void tryToOpenLine() throws FriendlyException {
		_line = AudioCommon.bestAvailableTargetDataLine();
		
		if (_line == null)
			throwFriendly("Unable to find a target data line for your mic.");
		
		try {
			_line.open();
		} catch (LineUnavailableException e) {
			_line = null;
			throwFriendly("Unable to open the data line for your mic.");
		}
		
		_line.start();
	}

	private void throwFriendly(String specifics) throws FriendlyException {
		throw new FriendlyException("Mic not working", specifics + " Try changing your operating system's mic and mixer settings.");
	}
}
