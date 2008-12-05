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

	private int _lastProduced = -1;
	
	private final SortedSet<Packet<T>> _buffer = new TreeSet<Packet<T>>(
		new Comparator<Packet<T>>(){@Override public int compare(Packet<T> p1, Packet<T> p2) {
			if(isDiferenceGreaterThanMaxGap(p2._sequence, p1._sequence) )
				return p1._sequence + p2._sequence; 
			
			return p1._sequence - p2._sequence;
	}});

	
	
	SequencerImpl(Consumer<? super T> consumer, short bufferSize, short maxGap) {
		_consumer = consumer;
		_bufferSize = bufferSize;
		_maxGap = maxGap;
	}


	@Override
	public synchronized void sequence(T element, short number) {
		Packet<T> packet = new Packet<T>(element, number);
		
		drainIfNecessary(packet);
		leftDrain(_lastProduced);
		_buffer.add(packet);
		resetingProductionSequence();
		drainOldPackets();
		produceUninterruptedPackets();
		produceInterruptedPackets();
	}

	private void drainIfNecessary(Packet<T> packet) {
		if (_buffer.isEmpty()) return;
		Packet<T> previous = _buffer.first();

		if(isDiferenceGreaterThanMaxGap(previous._sequence, packet._sequence)){
			_lastProduced = packet._sequence-1;
			leftDrain(previous._sequence);
		}
	}

	private void drainOldPackets() {
		Iterator<Packet<T>> iterator = _buffer.iterator();
		if(!iterator.hasNext()) return;
		Packet<T> previous = iterator.next();
		
		while (iterator.hasNext()) {
			Packet<T> packet = iterator.next();
			if(isDiferenceGreaterThanMaxGap(previous._sequence, packet._sequence)){
				_lastProduced = packet._sequence-1;
				leftDrain(previous._sequence);
				return;
			}
			previous = packet;
		}		
	}

	private void leftDrain(int limit) {
		Iterator<Packet<T>> iterator = _buffer.iterator();
		while (iterator.hasNext()) {
			Packet<T> packet = iterator.next();
			if(packet._sequence>limit) return;
			iterator.remove();
//			System.out.println("drain " + packet._sequence);
		}	
	}
	
	interface BreakCondition<T> {
		boolean evaluate(Packet<T> current);
	}
	
	private void resetingProductionSequence() {
		tryToProduceAndRemove(new BreakCondition<T>() {@Override public boolean evaluate(Packet<T> current) {
			return !isDiferenceGreaterThanMaxGap(lastProduced(), current._sequence);
		}});				
	}
	
	private void produceUninterruptedPackets() {
		tryToProduceAndRemove(new BreakCondition<T>() {@Override public boolean evaluate(Packet<T> current) {
			return nextSequenceToProduce() != current._sequence;
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
//		System.out.println("produce " + packet._sequence);
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
	
	private int nextSequenceToProduce() {
		return lastProduced()+1;
	}

	private int lastProduced() {
		if(_lastProduced>= Short.MAX_VALUE)
			_lastProduced = Short.MIN_VALUE-1;
		return _lastProduced;
	}
	
	private boolean isDiferenceGreaterThanMaxGap(int previousSequence, int packetSequence) {
		if(_lastProduced < Short.MIN_VALUE) return false;
		return Math.abs(packetSequence-previousSequence) > _maxGap; //This subtraction only works because shorts are promoted to int before subtraction
	}
}