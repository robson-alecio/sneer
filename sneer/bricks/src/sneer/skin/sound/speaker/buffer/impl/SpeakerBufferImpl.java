package sneer.skin.sound.speaker.buffer.impl;

import java.util.Deque;
import java.util.LinkedList;

import sneer.kernel.container.Inject;
import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.speaker.buffer.SpeakerBuffer;
import wheel.lang.Omnivore;
import wheel.lang.Threads;


class SpeakerBufferImpl implements SpeakerBuffer {

	@Inject
	static private ThreadPool _threads;
	
	private final Omnivore<? super PcmSoundPacket> _consumer;
	private boolean _isRunning = true;
	
	private final Deque<PcmSoundPacket> _queue = new LinkedList<PcmSoundPacket>();

	
	{
		_threads.registerStepper(new Stepper() { @Override public boolean step() {
			return doStep();
		}});
	}
	

	@Override
	public synchronized void crash() {
		_isRunning = false;
		notify();
	}
	
	
	public SpeakerBufferImpl(Omnivore<? super PcmSoundPacket> consumer) {
		_consumer = consumer;
	}


	@Override
	public synchronized void consume(PcmSoundPacket packet) {
		_queue.add(packet);
	}


	private synchronized boolean doStep() {
		if (!_isRunning) return false;
		
		if (_queue.isEmpty())
			Threads.waitWithoutInterruptions(this);
		
		if (!_isRunning) return false;

		_consumer.consume(_queue.pop());
		
		return true;
	}
	
	
}
