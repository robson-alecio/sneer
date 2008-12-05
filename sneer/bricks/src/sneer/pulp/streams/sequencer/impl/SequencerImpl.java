package sneer.pulp.streams.sequencer.impl;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.pulp.streams.sequencer.Sequencer;
import wheel.lang.Consumer;
import wheel.lang.Pair;

class SequencerImpl<T> implements Sequencer<T> {

	private static final int MAX_INTERRUPTED = 30;
	private static final int MAX_GAP = 500;

	private final Consumer<? super T> _consumer;

	private int _lastProduced = -1;
	
	private final SortedSet<Pair<T, Short>> _sortedSet = new TreeSet<Pair<T, Short>>(
		new Comparator<Pair<T, Short>>(){@Override public int compare(Pair<T, Short> pair1, Pair<T, Short> pair2) {
			if(isDiferenceGreaterThanMaxGap(pair2._b, pair1._b) )
				return pair1._b + pair2._b; 
			
			return pair1._b - pair2._b;
	}});

	public SequencerImpl(Consumer<T> consumer) {
		_consumer = consumer;
	}

	@Override
	public synchronized void sequence(T packet, short number) {
		Pair<T, Short> pair = new Pair<T, Short>(packet, number);
		if(number==nextSequenceToPlay()){
			produce(pair);
			playUninterruptedPackets();
			return;
		}
		
		drainIfNecessary(pair);
		leftDrain(_lastProduced);
		_sortedSet.add(pair);
		resetingSequencePlay();
		drainOldPackets();
		playUninterruptedPackets();
		playInterruptedPackets();
	}

	private void drainIfNecessary(Pair<T, Short> pair) {
		Iterator<Pair<T, Short>> iterator = _sortedSet.iterator();
		if(!iterator.hasNext()) return;
		Pair<T, Short> previous = iterator.next();

		if(isDiferenceGreaterThanMaxGap(previous._b, pair._b)){
			_lastProduced = pair._b-1;
			leftDrain(previous._b);
		}
	}

	private void drainOldPackets() {
		Iterator<Pair<T, Short>> iterator = _sortedSet.iterator();
		if(!iterator.hasNext()) return;
		Pair<T, Short> previous = iterator.next();
		
		while (iterator.hasNext()) {
			Pair<T, Short> packet = iterator.next();
			if(isDiferenceGreaterThanMaxGap(previous._b, packet._b)){
				_lastProduced = packet._b-1;
				leftDrain(previous._b);
				return;
			}
			previous = packet;
		}		
	}

	private void leftDrain(int limit) {
		Iterator<Pair<T, Short>> iterator = _sortedSet.iterator();
		while (iterator.hasNext()) {
			Pair<T, Short> packet = iterator.next();
			if(packet._b>limit) return;
			iterator.remove();
//			System.out.println("drain " + packet._b);
		}	
	}
	
	interface BreakCondition<T> {
		boolean evaluate(Pair<T, Short> currentPair);
	}
	
	private void resetingSequencePlay() {
		tryToPlayAndRemove(new BreakCondition<T>() {@Override public boolean evaluate(Pair<T, Short> currentPair) {
			return !isDiferenceGreaterThanMaxGap(lastPlayed(), currentPair._b);
		}});				
	}
	
	private void playUninterruptedPackets() {
		tryToPlayAndRemove(new BreakCondition<T>() {@Override public boolean evaluate(Pair<T, Short> currentPair) {
			return nextSequenceToPlay() != currentPair._b;
		}});
	}
	
	private void playInterruptedPackets() {
		if(_sortedSet.size()<2) return;
		Pair<T, Short> lastPacket = _sortedSet.last();
		final int maxSequenceToPlay = lastPacket._b - MAX_INTERRUPTED;
		tryToPlayAndRemove(new BreakCondition<T>() {@Override public boolean evaluate(Pair<T, Short> currentPair) {
			return currentPair._b >maxSequenceToPlay;
		}});		
	}
	
	private void produce(Pair<T, Short> pair) {
//		System.out.println("produce " + number);
		_lastProduced = pair._b;
		_consumer.consume(pair._a);
	}

	private void tryToPlayAndRemove(BreakCondition<T> breakCondition) {
		Iterator<Pair<T, Short>> iterator = _sortedSet.iterator();
		while (iterator.hasNext()) {
			Pair<T, Short> pair = iterator.next();
			if(breakCondition.evaluate(pair)) return;
			produce(pair);
			iterator.remove();
		}
	}
	
	private int nextSequenceToPlay() {
		return lastPlayed()+1;
	}

	private int lastPlayed() {
		if(_lastProduced>= Short.MAX_VALUE)
			_lastProduced = Short.MIN_VALUE-1;
		return _lastProduced;
	}
	
	private boolean isDiferenceGreaterThanMaxGap(int previousSequence, int packetSequence) {
		//This subtraction only works because shorts are promoted to int before subtraction
		if(_lastProduced<Short.MIN_VALUE) return false;
		return Math.abs(packetSequence-previousSequence)  > MAX_GAP;
	}
}