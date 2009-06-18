package sneer.bricks.hardware.cpu.threads.impl;

import java.util.concurrent.CountDownLatch;

import sneer.bricks.hardware.cpu.threads.Latch;

public class LatchImpl implements Latch {

	CountDownLatch _delegate = new CountDownLatch(1);
	
	@Override
	public void await() {
		try {
			_delegate.await();
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void run() {
		trip();
	}

	@Override
	public void trip() {
		_delegate.countDown();
	}

}
