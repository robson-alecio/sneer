package sneer.skin.sound.mic.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.exceptions.FriendlyException;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.retrier.Retrier;
import sneer.pulp.retrier.RetrierManager;
import sneer.pulp.retrier.Task;
import sneer.pulp.threads.Stepper;
import sneer.pulp.threads.Threads;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.mic.Mic;

class MicImpl implements Mic {

	private final Threads _threads = my(Threads.class);
	private final RetrierManager _retriers = my(RetrierManager.class);
	private final TupleSpace _tupleSpace = my(TupleSpace.class);
	
	private boolean _isOpen;
	private Stepper _refToAvoidGc;
	
	private Register<Boolean> _isRunning = my(Signals.class).newRegister(false);
	
	@Override
	public Signal<Boolean> isRunning() {
		return _isRunning.output();
	}

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
		if (_refToAvoidGc != null) return;

		_refToAvoidGc = new Stepper() { @Override public boolean step() {
			work();
			return false;
		}};

		_threads.registerStepper(_refToAvoidGc);
	}
	
	private void work() {
		while (true) {
			if (doCapture()) continue;
			if (doAcquireLine()) continue;
	
			MicLine.close();
			_isRunning.setter().consume(false);

			synchronized (this) {
				if (!_isOpen) {
					_refToAvoidGc = null;
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
		
		_tupleSpace.publish(new PcmSoundPacket(MicLine.read()));
		_isRunning.setter().consume(true);
		
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
		_threads.waitWithoutInterruptions(this);
	}
}