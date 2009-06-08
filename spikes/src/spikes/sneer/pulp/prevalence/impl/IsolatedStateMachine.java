package spikes.sneer.pulp.prevalence.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import spikes.sneer.pulp.prevalence.StateMachine;

class IsolatedStateMachine implements StateMachine {

	private final StateMachine _business;
	private final Lock _write;
	private final Lock _read;

	
	IsolatedStateMachine(StateMachine business) {
		_business = business;
		
		ReadWriteLock lock = new ReentrantReadWriteLock(true);
		_write = lock.writeLock();
		_read = lock.readLock();
	}

	
	@Override
	public Object changeState(Object event) {
		_write.lock();
		try {
			return _business.changeState(event);
		} finally {
			_write.unlock();
		}
	}

	@Override
	public Object queryState(Object query) {
		_read.lock();
		try {
			return _business.queryState(query);
		} finally {
			_read.unlock();
		}
	}

}
