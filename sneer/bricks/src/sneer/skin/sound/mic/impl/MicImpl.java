package sneer.skin.sound.mic.impl;

import sneer.kernel.container.Inject;
import sneer.pulp.retrier.Retrier;
import sneer.pulp.retrier.RetrierManager;
import sneer.pulp.retrier.Task;
import sneer.pulp.threadpool.ThreadPool;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.mic.Mic;
import wheel.lang.Threads;
import wheel.lang.exceptions.FriendlyException;

public class MicImpl implements Mic {

	@Inject
	static private ThreadPool _threads;
	@Inject
	static private RetrierManager _retriers;
	@Inject
	static private TupleSpace _tupleSpace;
	
	private boolean _isOpen;
	private Runnable _worker;

	
	@Override
	synchronized public void open() {
		_isOpen = true;
		startToWorkIfNecessary();
	}
	
	@Override
	synchronized public void close() {
		_isOpen = false;
		wakeUp();
	}

	private void startToWorkIfNecessary() {
		if (_worker != null) return;

		_worker = new Runnable(){ @Override public void run() {
			work();
		}};
		
		_threads.registerActor(_worker);
	}
	
	private void work() {
		while (true) {
			if (doCapture()) continue;
			if (doAcquireLine()) continue;
	
			MicLine.close();

			synchronized (this) {
				if (!_isOpen) {
					_worker = null;
					return;
				}
			}
		}
	}

	
	private boolean doAcquireLine() {
		Retrier retrier;
		synchronized (this) {
			if (!isOpen()) return false;

			retrier = _retriers.startRetrier(5000, new Task() { @Override public void execute() throws FriendlyException { //Fix The blinking light turned on by this retrier is redundant with the one turned on by the Audio brick. 
				MicLine.tryToAcquire();
				wakeUp();
			}});
		
			goToSleep();
		}
		
		retrier.giveUpIfStillTrying();
		return isOpen();
	}

	
	private boolean doCapture() {
		if (!isOpen()) return false;
		if (!MicLine.isAquired()) return false;
		
		_tupleSpace.publish(MicLine.read());
		
		return true;
	}


	private boolean isOpen() {
		synchronized (this) {
			return _isOpen;
		}
	}

	synchronized private void wakeUp() {
		notify();
	}

	private void goToSleep() {
		Threads.waitWithoutInterruptions(this);
	}


}
