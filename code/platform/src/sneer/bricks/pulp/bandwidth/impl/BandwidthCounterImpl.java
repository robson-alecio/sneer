package sneer.bricks.pulp.bandwidth.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.concurrent.atomic.AtomicInteger;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.clock.timer.Timer;
import sneer.bricks.hardware.cpu.threads.OldSteppable;
import sneer.bricks.pulp.bandwidth.BandwidthCounter;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;

class BandwidthCounterImpl implements BandwidthCounter {

	private final int CONSOLIDATION_TIME = 3000;
	
	private final Clock _clock = my(Clock.class);
	
	private final AtomicInteger sent = new AtomicInteger();
	private final AtomicInteger received  = new AtomicInteger();
	
	private final Register<Integer> _download = my(Signals.class).newRegister(0); 
	private final Register<Integer> _upload = my(Signals.class).newRegister(0); 
	
	private long _lastConsolidationTime;

	
	BandwidthCounterImpl(){
		_lastConsolidationTime = _clock.time().currentValue();
		my(Timer.class).wakeUpEvery(CONSOLIDATION_TIME, new OldSteppable(){ @Override public boolean step() {
			consolidate();
			return true;
		}});
	}
	
	@Override public Signal<Integer> downloadSpeed() { return _download.output(); }
	@Override public Signal<Integer> uploadSpeed() { return _upload.output();  }
	@Override public void received(int sizeBytes) { received.addAndGet(sizeBytes); }
	@Override public void sent(int sizeBytes) { sent.addAndGet(sizeBytes); }
	
	private final void consolidate(){
		long currentTime = _clock.time().currentValue();
		int deltaTime = (int) (currentTime-_lastConsolidationTime);
		int deltaTimeInSeconds =deltaTime/1000;
		
		_download.setter().consume(toKbytes(received, deltaTimeInSeconds));
		_upload.setter().consume(toKbytes(sent, deltaTimeInSeconds));
		
		_lastConsolidationTime = currentTime;
	}

	private int toKbytes(AtomicInteger sum, int deltaTimeInSeconds) {
		return sum.getAndSet(0) / (1024 * deltaTimeInSeconds);
	}
}