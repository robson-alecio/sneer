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

	private static final int MAX_INTERRUPTED = 30;
	private static final int MAX_GAP = 500;

	private final Omnivore<? super PcmSoundPacket> _consumer;
	private boolean _isRunning = true;

	private int _lastPlayed = -1;
	
	private final SortedSet<PcmSoundPacket> _sortedSet = new TreeSet<PcmSoundPacket>(new Comparator<PcmSoundPacket>(){@Override public int compare(PcmSoundPacket packet1, PcmSoundPacket packet2) {
		return packet1.sequence - packet2.sequence; 
	}});

	@Inject
	static private ThreadPool _threads; {
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
		
		leftDrain(_lastPlayed);
		drainOldPackets();
		playUninterruptedPackets();
		playInterruptedPackets();
		return true;
	}

	private void drainOldPackets() {
		Iterator<PcmSoundPacket> iterator = _sortedSet.iterator();
		if(!iterator.hasNext()) return;
		PcmSoundPacket previous = iterator.next();
		
		while (iterator.hasNext()) {
			PcmSoundPacket packet = iterator.next();
			if(packet.sequence  > previous.sequence + MAX_GAP){
				_lastPlayed = packet.sequence-1;
				leftDrain(previous.sequence);
				return;
			}
			previous = packet;
		}		
	}

	private void leftDrain(int limit) {
		Iterator<PcmSoundPacket> iterator = _sortedSet.iterator();
		while (iterator.hasNext()) {
			PcmSoundPacket packet = iterator.next();
			if(packet.sequence>limit) return;
			iterator.remove();
			continue;
		}	
	}

	private void playInterruptedPackets() {
		if(_sortedSet.size()<2) return;
		PcmSoundPacket lastPacket = _sortedSet.last();
		int maxSequenceToPlay = lastPacket.sequence - MAX_INTERRUPTED;
		Iterator<PcmSoundPacket> iterator = _sortedSet.iterator();
		while (iterator.hasNext()) {
			PcmSoundPacket packet = iterator.next();
			if(packet.sequence>maxSequenceToPlay) return;
			play(packet);
			iterator.remove();
		}	
	}

	private void playUninterruptedPackets() {
		Iterator<PcmSoundPacket> iterator = _sortedSet.iterator();
		while (iterator.hasNext()) {
			PcmSoundPacket packet = iterator.next();
			if(nextSequenceToPlay() != packet.sequence) return;
			play(packet);
			iterator.remove();
		}
	}
	
	private void play(PcmSoundPacket packet) {
		_lastPlayed = packet.sequence;
		_consumer.consume(packet);
	}

	private int nextSequenceToPlay() {
		return _lastPlayed+1;
	}
}

class ReverseSequence extends PcmSoundPacket{

	ReverseSequence(PcmSoundPacket packet, int seq) {
		super(packet.publisher(), packet.publicationTime(), packet.payload, seq);
	}
}