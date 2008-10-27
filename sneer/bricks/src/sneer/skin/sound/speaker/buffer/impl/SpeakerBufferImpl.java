package sneer.skin.sound.speaker.buffer.impl;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

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
	
	private final SortedSet<PcmSoundPacket> _sortedSet = new TreeSet<PcmSoundPacket>(new Comparator<PcmSoundPacket>(){@Override public int compare(PcmSoundPacket packet1, PcmSoundPacket packet2) {
		return packet1.sequence - packet2.sequence; 
	}});

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
		_sortedSet.add(packet);
	}

	private synchronized boolean doStep() {
		if (!_isRunning) return false;
		
		if (_sortedSet.isEmpty())
			Threads.waitWithoutInterruptions(this);
		
		if (!_isRunning) return false;
		
		Iterator<PcmSoundPacket> iterator = _sortedSet.iterator();
		_consumer.consume(iterator.next());
		iterator.remove();
		
		return true;
	}
}