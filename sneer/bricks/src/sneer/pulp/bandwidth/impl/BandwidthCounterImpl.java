package sneer.pulp.bandwidth.impl;

import static wheel.lang.Environments.my;

import java.util.concurrent.atomic.AtomicInteger;

import sneer.pulp.bandwidth.BandwidthCounter;
import sneer.pulp.clock.Clock;
import sneer.pulp.threadpool.Stepper;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class BandwidthCounterImpl implements BandwidthCounter {

	private final int CONSOLIDATION_TIME = 1000;
	
	private final Clock _clock = my(Clock.class);
	
	private final AtomicInteger sended = new AtomicInteger();
	private final AtomicInteger received  = new AtomicInteger();
	
	private final Register<Integer> _download = new RegisterImpl<Integer>(0); 
	private final Register<Integer> _upload = new RegisterImpl<Integer>(0); 
	
	BandwidthCounterImpl(){
		_clock.wakeUpEvery(CONSOLIDATION_TIME, new Stepper(){ @Override public boolean step() {
			consolidate();
			return true;
		}});
	}
	
	@Override public Signal<Integer> downloadSpeed() { return _download.output(); }
	@Override public Signal<Integer> uploadSpeed() { return _upload.output();  }
	@Override public void received(int sizeBytes) { received.addAndGet(sizeBytes); }
	@Override public void sent(int sizeBytes) { sended.addAndGet(sizeBytes); }
	
	private final void consolidate(){
		_download.setter().consume(received.getAndSet(0)/1024);
		_upload.setter().consume(sended.getAndSet(0)/1024);
	}
}