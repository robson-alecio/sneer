package sneer.bricks.skin.audio.mic.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.ram.arrays.ImmutableByteArray;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.retrier.Retrier;
import sneer.bricks.pulp.retrier.RetrierManager;
import sneer.bricks.pulp.retrier.Task;
import sneer.bricks.skin.audio.mic.Mic;
import sneer.foundation.lang.exceptions.FriendlyException;

class MicImpl implements Mic {

	private final Threads _threads = my(Threads.class);
	private final RetrierManager _retriers = my(RetrierManager.class);
	
	private boolean _isOpenRequested;
	private Steppable _refToAvoidGc;
	
	private Register<Boolean> _isOpen = my(Signals.class).newRegister(false);
	private EventNotifier<ImmutableByteArray> _sound = my(EventNotifiers.class).newInstance();
	
	@Override
	public Signal<Boolean> isOpen() {
		return _isOpen.output();
	}

	@Override
	synchronized public void open() {
		_isOpenRequested = true;
		startToWorkIfNecessary();
	}
	
	@Override
	synchronized public void close() {
		_isOpenRequested = false;
		wakeUp();
	}

	private void startToWorkIfNecessary() {
		if (_refToAvoidGc != null) return;

		_refToAvoidGc = new Steppable() { @Override public boolean step() {
			work();
			return false;
		}};
		_threads.newStepper(_refToAvoidGc);
	}
	
	private void work() {
		while (true) {
			if (doCapture()) continue;
			if (doAcquireLine()) continue;
	
			MicLine.close();
			_isOpen.setter().consume(false);

			synchronized (this) {
				if (!_isOpenRequested) {
					_refToAvoidGc = null;
					return;
				}
			}
		}
	}

	
	private boolean doAcquireLine() {
		Retrier retrier;
		synchronized (this) {
			if (!isOpenRequested()) return false;

			retrier = _retriers.startRetrier(5000, new Task() { @Override public void execute() throws FriendlyException { //Fix The blinking light turned on by this retrier is redundant with the one turned on by the Audio brick. 
				MicLine.tryToAcquire();
				wakeUp();
			}});
		
			goToSleep();
		}
		
		retrier.giveUpIfStillTrying();
		return isOpenRequested();
	}

	
	private boolean doCapture() {
		if (!isOpenRequested()) return false;
		if (!MicLine.isAquired()) return false;
		
		_sound.notifyReceivers(MicLine.read());
		_isOpen.setter().consume(true);
		
		return true;
	}


	private boolean isOpenRequested() {
		synchronized (this) {
			return _isOpenRequested;
		}
	}

	synchronized private void wakeUp() {
		notify();
	}

	private void goToSleep() {
		_threads.waitWithoutInterruptions(this);
	}

	@Override
	public EventSource<ImmutableByteArray> sound() {
		return _sound.output();
	}
}