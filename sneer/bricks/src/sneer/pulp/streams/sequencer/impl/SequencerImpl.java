package sneer.pulp.streams.sequencer.impl;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.pulp.streams.sequencer.Sequencer;
import wheel.lang.Consumer;

class SequencerImpl<T> implements Sequencer<T> {

	private final short _bufferSize;
	private final short _maxGap;

	private final Consumer<? super T> _consumer;

	private short _lastProduced = -1;
	
	private final SortedSet<Packet<T>> _buffer = new TreeSet<Packet<T>>(
		new Comparator<Packet<T>>(){@Override public int compare(Packet<T> p1, Packet<T> p2) {
			return cyclicCompare(p1._sequence, p2._sequence);
		}}
	);
	private short _incomingSequence;

	
	
	SequencerImpl(Consumer<? super T> consumer, short bufferSize, short maxGap) {
		_consumer = consumer;
		_bufferSize = bufferSize;
		_maxGap = maxGap;
	}


	@Override
	public synchronized void sequence(T element, short number) {
		_incomingSequence = number;

		if(isDiferenceGreaterThanMaxGap(_lastProduced, _incomingSequence)) {
			drain();
		} else {
			if (wasAlreadyProduced())
				return;
		}

		Packet<T> packet = new Packet<T>(element, number);
		_buffer.add(packet);
		
		
		Iterator<Packet<T>> it = _buffer.iterator();
		while (it.hasNext()) {
			Packet<T> candidate = it.next();
			if (shouldProduce(candidate)) {
				produce(candidate);
				it.remove();
			}
		}
		
		
		produceUninterruptedPackets();
		produceInterruptedPackets();
	}


	private boolean shouldProduce(Packet<T> candidate) {
		if (candidate._sequence == _lastProduced + 1) return true;
				
		short s1 = candidate._sequence;
		short in = _incomingSequence;
		
		if (s1 < 0 != in < 0) {
			s1 += _maxGap;
			in += _maxGap;
		}
		
		return (s1 - in) > _bufferSize;
	}


	private boolean wasAlreadyProduced() {
		boolean result = cyclicCompare(_lastProduced, _incomingSequence) >= 0;
		if (result)
			System.out.println("Already Produced: " + _incomingSequence + " (lastProduced: "+_lastProduced+")");
		return result;
	}


	private void drain() {
		System.out.println("Drained: " + _buffer);
		_buffer.clear();
	}


	interface BreakCondition<T> {
		boolean evaluate(Packet<T> current);
	}
	
	
	private void produceUninterruptedPackets() {
		tryToProduceAndRemove(new BreakCondition<T>() {@Override public boolean evaluate(Packet<T> current) {
			return _lastProduced + 1 != current._sequence;
		}});
	}
	
	private void produceInterruptedPackets() {
		if(_buffer.size()<2) return;
		Packet<T> lastPacket = _buffer.last();
		final int maxSequenceToProduce = lastPacket._sequence - _bufferSize;
		tryToProduceAndRemove(new BreakCondition<T>() {@Override public boolean evaluate(Packet<T> currentPacket) {
			return currentPacket._sequence > maxSequenceToProduce;
		}});		
	}
	
	private void produce(Packet<T> packet) {
		System.out.println("produce " + packet._sequence);
		_lastProduced = packet._sequence;
		_consumer.consume(packet._payload);
	}

	private void tryToProduceAndRemove(BreakCondition<T> breakCondition) {
		Iterator<Packet<T>> iterator = _buffer.iterator();
		while (iterator.hasNext()) {
			Packet<T> packet = iterator.next();
			if(breakCondition.evaluate(packet)) return;
			produce(packet);
			iterator.remove();
		}
	}
	
	
	private boolean isDiferenceGreaterThanMaxGap(short s1, short s2) {
		if (s1 < 0 != s2 < 0) {
			s1 += _maxGap;
			s2 += _maxGap;
		}

		System.out.println("Gap: " + s1 + ", " + s2);

		return (Math.abs(s1 - s2) > _maxGap);
	}


	private int cyclicCompare(short s1, short s2) {
		if (s1 < 0 != s2 < 0) {
			s1 += _maxGap;
			s2 += _maxGap;
		}
		
		return s1 - s2;
	}
}