package sneer.skin.sound.speaker.buffer.impl;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.speaker.buffer.SpeakerBuffer;
import wheel.lang.Consumer;

class SpeakerBufferImpl implements SpeakerBuffer {

	private static final int MAX_INTERRUPTED = 30;
	private static final int MAX_GAP = 500;

	private final Consumer<? super PcmSoundPacket> _consumer;

	private int _lastPlayed = -1;
	
	private final SortedSet<PcmSoundPacket> _sortedSet = new TreeSet<PcmSoundPacket>(
		new Comparator<PcmSoundPacket>(){@Override public int compare(PcmSoundPacket packet1, PcmSoundPacket packet2) {
			if(isDiferenceGreaterThanMaxGap(packet2, packet1) )
				return packet1.sequence + packet2.sequence; 
			
			return packet1.sequence - packet2.sequence; 
	}});

	public SpeakerBufferImpl(Consumer<? super PcmSoundPacket> consumer) {
		_consumer = consumer;
	}

	@Override
	public synchronized void consume(PcmSoundPacket packet) {
		if(packet.sequence==nextSequenceToPlay()){
			play(packet);
			playUninterruptedPackets();
			return;
		}
		
		drainIfNecessary(packet);
		leftDrain(_lastPlayed);
		_sortedSet.add(packet);
		resetingSequencePlay();
		drainOldPackets();
		playUninterruptedPackets();
		playInterruptedPackets();
	}

	private void drainIfNecessary(PcmSoundPacket packet) {
		Iterator<PcmSoundPacket> iterator = _sortedSet.iterator();
		if(!iterator.hasNext()) return;
		PcmSoundPacket previous = iterator.next();

		if(isDiferenceGreaterThanMaxGap(previous, packet)){
			_lastPlayed = packet.sequence-1;
			leftDrain(previous.sequence);
		}
	}

	private void drainOldPackets() {
		Iterator<PcmSoundPacket> iterator = _sortedSet.iterator();
		if(!iterator.hasNext()) return;
		PcmSoundPacket previous = iterator.next();
		
		while (iterator.hasNext()) {
			PcmSoundPacket packet = iterator.next();
			if(isDiferenceGreaterThanMaxGap(previous, packet)){
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
//			System.out.println("drain " + packet.sequence);
		}	
	}
	
	interface BreakCondition {
		boolean evaluate(PcmSoundPacket currentPacket);
	}
	
	private void resetingSequencePlay() {
		tryToPlayAndRemove(new BreakCondition() {@Override public boolean evaluate(PcmSoundPacket currentPacket) {
			return !isDiferenceGreaterThanMaxGap(lastPlayed(), currentPacket.sequence);
		}});				
	}
	
	private void playUninterruptedPackets() {
		tryToPlayAndRemove(new BreakCondition() {@Override public boolean evaluate(PcmSoundPacket currentPacket) {
			return nextSequenceToPlay() != currentPacket.sequence;
		}});
	}
	
	private void playInterruptedPackets() {
		if(_sortedSet.size()<2) return;
		PcmSoundPacket lastPacket = _sortedSet.last();
		final int maxSequenceToPlay = lastPacket.sequence - MAX_INTERRUPTED;
		tryToPlayAndRemove(new BreakCondition() {@Override public boolean evaluate(PcmSoundPacket currentPacket) {
			return currentPacket.sequence>maxSequenceToPlay;
		}});		
	}
	
	private void play(PcmSoundPacket packet) {
//		System.out.println("play " + packet.sequence);
		_lastPlayed = packet.sequence;
		_consumer.consume(packet);
	}

	private void tryToPlayAndRemove(BreakCondition breakCondition) {
		Iterator<PcmSoundPacket> iterator = _sortedSet.iterator();
		while (iterator.hasNext()) {
			PcmSoundPacket packet = iterator.next();
			if(breakCondition.evaluate(packet)) return;
			play(packet);
			iterator.remove();
		}
	}
	
	private int nextSequenceToPlay() {
		return lastPlayed()+1;
	}

	private int lastPlayed() {
		if(_lastPlayed>= Short.MAX_VALUE)
			_lastPlayed = Short.MIN_VALUE-1;
		return _lastPlayed;
	}
	
	private boolean isDiferenceGreaterThanMaxGap(PcmSoundPacket previous, PcmSoundPacket packet) {
		return isDiferenceGreaterThanMaxGap(previous.sequence, packet.sequence);
	}
	
	private boolean isDiferenceGreaterThanMaxGap(int previousSequence, int packetSequence) {
		//This subtraction only works because shorts are promoted to int before subtraction
		if(_lastPlayed<Short.MIN_VALUE) return false;
		return Math.abs(packetSequence-previousSequence)  > MAX_GAP;
	}
}