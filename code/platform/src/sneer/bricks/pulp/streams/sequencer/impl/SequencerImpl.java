package sneer.bricks.pulp.streams.sequencer.impl;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.bricks.pulp.streams.sequencer.Sequencer;
import sneer.foundation.lang.Consumer;

class SequencerImpl<T> implements Sequencer<T> {

	private final short _bufferSize;
	private final short _maxGap;

	private final Consumer<? super T> _consumer;

	private final SortedSet<Packet<T>> _buffer = new TreeSet<Packet<T>>(
		new Comparator<Packet<T>>(){@Override public int compare(Packet<T> p1, Packet<T> p2) {
			return shortSubtract(p1._sequence, p2._sequence);
		}}
	);

	private short _incomingSequence;
	private short _lastProduced = -1;
	
	
	SequencerImpl(Consumer<? super T> consumer, short bufferSize, short maxGap) {
		_consumer = consumer;
		_bufferSize = bufferSize;
		_maxGap = maxGap;
	}


	@Override
	public synchronized void produceInSequence(T element, short sequence) {
		_incomingSequence = sequence;
		Packet<T> packet = new Packet<T>(element, sequence);

		if (isGapTooBig()) {
			_buffer.clear();
			produce(packet);
			return;
		}
		
		if (wasAlreadyProduced()) return;

		_buffer.add(packet);
		producePacketsInBuffer();
	}


	private void producePacketsInBuffer() {
		Iterator<Packet<T>> it = _buffer.iterator();
		while (it.hasNext()) {
			Packet<T> candidate = it.next();
			if (shouldProduce(candidate)) {
				produce(candidate);
				it.remove();
			}
		}
	}


	private boolean shouldProduce(Packet<T> candidate) {
		if (shortSubtract(candidate._sequence, (short)1) == _lastProduced) return true;
				
		return (shortSubtract(_incomingSequence, candidate._sequence)) > _bufferSize;
	}


	private boolean wasAlreadyProduced() {
		return shortSubtract(_incomingSequence, _lastProduced) <= 0;
	}


	private void produce(Packet<T> packet) {
		_lastProduced = packet._sequence;
		_consumer.consume(packet._payload);
	}


	private boolean isGapTooBig() {
		int gap = Math.abs(shortSubtract(_lastProduced, _incomingSequence));
		return gap > _maxGap;
	}


	/** Subtracts two shorts without Java's default promotion to int. Normal signed 16-bit overflow semantics are allowed to happen, as one would expect.*/
	private short shortSubtract(short s1, short s2) {
		return (short)(s1 - s2);
	}
}