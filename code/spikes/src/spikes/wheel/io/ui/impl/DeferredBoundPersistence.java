package spikes.wheel.io.ui.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.Rectangle;
import java.util.concurrent.atomic.AtomicBoolean;

import sneer.bricks.hardware.cpu.threads.Threads;
import spikes.wheel.io.ui.BoundsPersistence;

public class DeferredBoundPersistence implements BoundsPersistence {

	private final BoundsPersistence _persistence;
	
	private final Thread _boundsKeeperThread;
	private final AtomicBoolean _isDirty;
	
	public DeferredBoundPersistence(BoundsPersistence persistence){
		_persistence = persistence;		
		_isDirty = new AtomicBoolean(false);
		_boundsKeeperThread = new Thread(new PersistBounds());
		_boundsKeeperThread.start();
	}
	
	
	private final class PersistBounds implements Runnable {


		@Override
		public void run() {
			while(true){ 
				
				if (_isDirty.getAndSet(false))
					deferredWriteBoundsToDisk();
				
				synchronized (_isDirty){
					if (!_isDirty.get())
						my(Threads.class).waitWithoutInterruptions(_isDirty);
				}
			
			}
				
		}

		private void deferredWriteBoundsToDisk() {
			int diskWriteThreshold = 1000;
			my(Threads.class).sleepWithoutInterruptions(diskWriteThreshold);
			if (_isDirty.getAndSet(false)){
				deferredWriteBoundsToDisk();
				return;
			}
			
			_persistence.store();				
		}

	}


	@Override
	public synchronized Rectangle getStoredBounds(String id) {
		return _persistence.getStoredBounds(id);
	}

	@Override
	public synchronized void setBounds(String id, Rectangle bounds) {
		_persistence.setBounds(id, bounds);
	}

	@Override
	public synchronized void store() {
		synchronized (_isDirty) {
			_isDirty.set(true);
			_isDirty.notify();			
		}		
	}

}
